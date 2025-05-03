import { useContext, useEffect, useState } from "react";
import "./Home.css";
import locale from "antd/es/date-picker/locale/es_ES";
import placesOptions from "./options";
import { NotificationContext } from "../../context/NotificationContext";
import { useCategory } from "../../hooks/useCategory";
import { useAccommodation } from "../../hooks/useAccommodation";
import { useNavigate } from "react-router-dom";
import { AutoComplete, DatePicker, Empty, Skeleton } from "antd";
import { SearchOutlined } from "@ant-design/icons";
import { CategoryCard } from "../../components/CategoryCard/CategoryCard";
import { AccommodationSkeleton } from "../../components/AccommodationSkeleton/AccommodationSkeleton";
import { AccommodationCard } from "../../components/AccommodationCard/AccommodationCard";
import { IoSearchSharp } from "react-icons/io5";
const Home = () => {
  const [options] = useState(placesOptions);
  const { toaster } = useContext(NotificationContext);
  const {
    categories,
    getAllCategories,
    isLoading: isLoadingCategories,
    error: errorCategories,
    success: successCategories,
  } = useCategory();
  const {
    accommodations,
    getRandomAccommodations,
    isLoading: isLoadingAccommodations,
    error: errorAccommodations,
    success: successAccommodation,
  } = useAccommodation();

  const [dateRange, setDateRange] = useState([]);
  const [place, setPlace] = useState("");
  const navigate = useNavigate();

  const { RangePicker } = DatePicker;

  const handleDateRangeOnChange = (dateString) => {
    setDateRange(dateString);
  };

  const handleSearch = (newValue) => {
    setPlace(newValue);
  };
  const handleOnSubmmit = (e) => {
    e.preventDefault();
    if (place === "" && (dateRange === null || dateRange.length === 0)) {
      toaster["error"]({
        message: "Debe elegir al menos un lugar.",
        description: "Por favor, seleccione un lugar.",
        duration: 3,
      });
      return;
    }
    let url = "/search?";
    place && (url += `place=${place}&`);
    dateRange &&
      dateRange.length > 0 &&
      (url += `checkIn=${dateRange[0].format(
        "YYYY-MM-DD"
      )}&checkOut=${dateRange[1].format("YYYY-MM-DD")}`);
    navigate(url);
  };

  useEffect(() => {
    getAllCategories();
    getRandomAccommodations();
  }, []);
  
  return (
    <main className="home-main">
         <section className="home-main-section">
      <div>
        <h1>El hogar de tu próxima experiencia te espera</h1>
        <form className="main-section-form" onSubmit={handleOnSubmmit}>
          <label htmlFor="place">
            <AutoComplete
            style={{width:"100%"}}
              id="place"
              allowClear
              className="form-multiple-select"
              onChange={handleSearch}
              options={options}
              filterOption={(inputValue, option) =>
                option.value.toUpperCase().indexOf(inputValue.toUpperCase()) !== -1
              }
              placeholder="Seleccione un destino"
              listHeight={128}
            />
          </label>
          <label htmlFor="date">
            <RangePicker
              id="date"
              className="form-date-range-picker"
              onChange={handleDateRangeOnChange}
              format={"DD/MM/YYYY"}
              placeholder={["CheckIn", "CheckOut"]}
              locale={locale}
              disabledDate={(current) => current && current < new Date()}
            />
          </label>
          <button className="button button-base">
            <IoSearchSharp />
          </button>
        </form>
      </div>
        </section>
     
        <section className="home-category-section">
          <h2>Elegi por categoria</h2>
          {isLoadingCategories ? (
            <div className="category-section-container">
              <Skeleton
                active
                title={{ width: "100%", style: { height: "200px" } }}
                paragraph={{ rows: 0 }}
              />
            </div>
          ) : errorCategories ? (
            <Empty description="No se han podido obtener las categorias." />
          ) : categories && categories.length > 0 ? (
            <div className="category-section-container">
              {categories.map((category) => (
                <CategoryCard key={category.id} category={category} />
              ))}
            </div>
          ) : (
            <Empty description="No hay categorias disponibles" />
          )}
        </section>
        <section className="home-recommendation-section">
          <h2>Lo mejor para tu próxima escapada</h2>
          {
            isLoadingAccommodations ? (
              <div className="recommendation-section-container">
                <AccommodationSkeleton/>
                <AccommodationSkeleton/>
                <AccommodationSkeleton/>
                <AccommodationSkeleton/>
              </div>
            )
            : errorAccommodations ? (
              <Empty description="Ocurrio un error obteniendo los alojamientos."/>
            )
            : successAccommodation && accommodations.length > 0 ? (
              <div className="recommendation-section-container">
                {
                  accommodations.map((accommodation)=>(
                    <AccommodationCard key={accommodation.id} accommodation={accommodation}/>
                  ))
                }
              </div>
            )
            :
            <Empty description="No hay alojamientos disponibles"/>
          }
        </section>

    </main>
  );
};

export default Home;