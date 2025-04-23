import React, { useContext, useState } from "react";
import { FormModalContext } from "../../../context/FormModalContext";
import { useAccommodation } from "../../../hooks/useAccommodation";
import { AccommodationForm } from "../../components/AccommodationForm/AccommodationForm";
import { PaginateItems } from "../../../ui/PaginateItems/PaginateItems";
import { Modal } from "../../../ui/Modal/Modal";

export const AccommodationList = () => {
  const { handleShowModal } = useContext(FormModalContext);
  const {
    accommodations,
    getAccommodations,
    deleteAccommodation,
    isLoading,
    totalPages,
    error,
  } = useAccommodation();
  const [contentModal, setContentModal] = useState(null);
  const [refetch, setRefetch] = useState(false);

  const onRefetch = () => {
    setRefetch((prev) => !prev);
  };

  const addAccommodation = () => {
    setContentModal(
      <AccommodationForm title={"Agregar Alojamiento"} onRefetch={onRefetch} />
    );
    handleShowModal();
  };

  const editAccommodation = (accommodation) => {
    setContentModal(
      <AccommodationForm
        title={"Editar Alojamiento"}
        accommodation={accommodation}
        onRefetch={onRefetch}
      />
    );
    handleShowModal();
  };
  return (
    <main className="dashboard-list-container">
      <header className="dashboard-header">
        <h2>Alojamientos</h2>
      </header>
      <button
        className="button button-primary"
        onClick={() => addAccommodation()}
      >
        Agregar Alojamiento
      </button>
      <section className="dashboard-list-item-section">
        <PaginateItems
          fetchData={getAccommodations}
          isLoading={isLoading}
          data={accommodations?.data}
          totalPages={totalPages}
          deleteItem={deleteAccommodation}
          error={error}
          editItem={editAccommodation}
          refetch={refetch}
          onRefetch={onRefetch}
        />
      </section>
      <Modal>{contentModal}</Modal>
    </main>
  );
};
