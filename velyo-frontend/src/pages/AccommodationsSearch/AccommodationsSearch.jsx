import { AutoComplete, DatePicker, Empty, Select } from "antd";
import "./AccommodationsSearch.css";
import { ArrayParam, StringParam, useQueryParams } from "use-query-params";
import { useCategory } from "../../hooks/useCategory";
import { useAccommodation } from "../../hooks/useAccommodation";
import { useEffect, useState } from "react";
import { useDebounce } from "@uidotdev/usehooks";
import locale from "antd/es/date-picker/locale/es_ES";
import dayjs from "dayjs";
import { Popover } from "../../ui/Popover/Popover";
import { AccommodationSkeleton } from "../../components/AccommodationSkeleton/AccommodationSkeleton";
import { AccommodationCard } from "../../components/AccommodationCard/AccommodationCard";

export const AccommodationsSearch = () => {
  const { RangePicker } = DatePicker;
  const [query, setQuery] = useQueryParams({ 
    checkIn: StringParam, 
    checkOut: StringParam, 
    place: StringParam, 
    categories: ArrayParam 
  });

  // Estados para las opciones
  const [categoryOptions, setCategoryOptions] = useState([]);
  const [placeOptions, setPlaceOptions] = useState([]);
  const [place, setPlace] = useState(query.place ? query.place : null);

  const debouncedPlace = useDebounce(place, 500);

  const {
    categories,
    getAllCategories,
    isLoading: isLoadingCategories,
    error: errorCategories,
  } = useCategory();

  const {
    accommodations,
    searchAccommodations,
    getAccommodations,
    isLoading: isLoadingAccommodations,
    error: errorAccommodations,
  } = useAccommodation();

  useEffect(() => {
    setQuery((prev) => ({ ...prev }));
    getAllCategories();
  }, []);

  // Configurar opciones de categorías
  useEffect(() => {
    if (categories) {
      const catOptions = categories.map((category) => ({
        label: category.name,
        value: category.id,
      }));
      setCategoryOptions(catOptions);
    }
  }, [categories]);

  // Configurar opciones de lugares
  useEffect(() => {
    if (accommodations?.data?.length > 0) {
      const uniquePlaces = new Map();

      accommodations.data.forEach((acc) => {
        const { city, country } = acc.address || {};
        [city, country].forEach((place) => {
          if (place) {
            const normalized = place.trim().toLowerCase();
            if (!uniquePlaces.has(normalized)) {
              uniquePlaces.set(normalized, place.trim());
            }
          }
        });
      });

      const placeOpts = Array.from(uniquePlaces.values())
        .sort((a, b) => a.localeCompare(b))
        .map((place) => ({ value: place }));

      setPlaceOptions(placeOpts);
    }
  }, [accommodations]);

  const handleCategoriesChange = (values) => {
    setQuery((prev) => ({ ...prev, categories: values }));
  };

  useEffect(() => {
    if (!debouncedPlace) {
      setQuery((prev) => ({ ...prev, place: undefined }));
      return;
    }
    setQuery((prev) => ({ ...prev, place: debouncedPlace }));
  }, [debouncedPlace]);

  const handlePlaceChange = (value) => {
    setPlace(value);
  };

  const handleDateRangeOnChange = (dateRange) => {
    if (!dateRange) {
      setQuery((prev) => ({ ...prev, checkIn: undefined, checkOut: undefined }));
      return;
    }

    const [start, end] = dateRange;
    setQuery((prev) => ({
      ...prev,
      checkIn: start.format("YYYY-MM-DD"),
      checkOut: end.format("YYYY-MM-DD"),
    }));
  };

  useEffect(() => {
    const hasSearchParams = query.place || query.checkIn || query.checkOut || 
                          (query.categories && query.categories.length > 0);
    
    if (!hasSearchParams) {
      getAccommodations();
    } else {
      searchAccommodations(
        query.categories,
        query.place,
        query.checkIn,
        query.checkOut
      );
    }
  }, [query]);

  return (
    <main className="search-container">
      <section className="search-filters">
        <p>Filtrar por:</p>
        <form>
          <div className="search-form-container">
            <div>
              <label htmlFor="place">Destino</label>
              <AutoComplete
                style={{ width: "100%" }}
                id="place"
                allowClear
                className="form-multiple-select"
                onChange={handlePlaceChange}
                options={placeOptions}
                filterOption={(inputValue, option) =>
                  option.value.toUpperCase().indexOf(inputValue.toUpperCase()) !== -1
                }
                placeholder="Seleccione un destino"
                listHeight={128}
              />
            </div>
            <div>
              <label htmlFor="categories">Categoria</label>
              <Select
                placeholder="Tipo de alojamiento"
                id="categorias"
                className="form-element"
                options={categoryOptions}
                mode="multiple"
                value={query.categories}
                onChange={handleCategoriesChange}
                allowClear
                disabled={isLoadingCategories || errorCategories}
              />
            </div>
            <div>
              <label htmlFor="date">Fechas disponibles</label>
              <RangePicker
                id="date"
                className="form-date-range-picker form-element"
                onChange={handleDateRangeOnChange}
                value={[
                  query.checkIn && dayjs(query.checkIn),
                  query.checkOut && dayjs(query.checkOut),
                ]}
                format={"DD/MM/YYYY"}
                placeholder={["CheckIn", "CheckOut"]}
                locale={locale}
              />
            </div>
          </div>
        </form>
      </section>
      <section className="search-acc-container">
        <div className="search-info">
          {accommodations && accommodations.totalElements > 0 && (
            <p>{accommodations.totalElements} Alojamientos encontrados</p>
          )}
          <Popover
            content={
              <Select
                placeholder="Tipo de alojamiento"
                id="categorias"
                className="form-element"
                options={categoryOptions}
                mode="multiple"
                value={query.categories}
                onChange={handleCategoriesChange}
                allowClear
                disabled={isLoadingCategories || errorCategories}
              />
            }
          >
            <button className="button button-primary filters-button">Filtros</button>
          </Popover>
        </div>
        <section className="acc-search">
          {isLoadingAccommodations ? (
            <>
              <AccommodationSkeleton />
              <AccommodationSkeleton />
              <AccommodationSkeleton />
              <AccommodationSkeleton />
            </>
          ) : errorAccommodations ? (
            <Empty description="No se han podido cargar los alojamientos" />
          ) : accommodations?.data && accommodations.data.length > 0 ? (
            accommodations.data.map((accommodation) => (
              <AccommodationCard
                key={accommodation.id}
                accommodation={accommodation}
              />
            ))
          ) : (
            <Empty description="No encontramos alojamientos que coincidan con tus parámetros de búsqueda." />
          )}
        </section>
      </section>
    </main>
  );
};