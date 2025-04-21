import React, { useContext, useState } from "react";
import { FormModalContext } from "../../../context/FormModalContext";
import { useAmenity } from "../../../hooks/useAmenity";
import { AmenityForm } from "../../components/AmenityForm/AmenityForm";
import { PaginateItems } from "../../../ui/PaginateItems/PaginateItems";
import { Modal } from "../../../ui/Modal/Modal";

export const AmenityList = () => {
  const { handleShowModal } = useContext(FormModalContext);
  const {
    amenities,
    getAmenities,
    deleteAmenity,
    isLoading,
    totalPages,
    error,
  } = useAmenity();
  const [contentModal, setContentModal] = useState(null);
  const [refetch, setRefetch] = useState(false);

  const onRefetch = () => {
    setRefetch((prev) => !prev);
  };

  const addAmenity = () => {
    setContentModal(
      <AmenityForm title={"Agregar Caracteristica"} onRefetch={onRefetch} />
    );
    handleShowModal();
  };

  const editAmenity = (amenity) => {
    setContentModal(
      <AmenityForm
        title={"Editar caracteristica"}
        onRefetch={onRefetch}
        amenity={amenity}
      />
    );
    handleShowModal;
  };
  return (
    <main className="dashboard-list-container">
      <header className="dashboard-header">
        <h2>Caracteristicas</h2>
      </header>

      <button className="button button-primary" onClick={() => addAmenity()}>
        Agregar Caracteristca
      </button>
      <section className="dashboard-list-item-section">
        <PaginateItems
         fetchData={getAmenities}
         loading={isLoading}
         data={amenities?.data}
         totalPages={totalPages}
         deleteItem={deleteAmenity}
         error={error}
         editItem={editAmenity}
         refetch={refetch}
         onRefetch={onRefetch}
        />
      </section>
      <Modal>
        {contentModal}
      </Modal>
    </main>
  );
};
