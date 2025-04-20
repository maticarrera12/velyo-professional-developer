import React, { useContext, useState } from 'react'
import { FormModalContext } from '../../../context/FormModalContext'
import { useCategory } from '../../../hooks/useCategory'
import { CategoryForm } from '../../components/CategoryForm/CategoryForm'
import { PaginateItems } from '../../../ui/PaginateItems/PaginateItems'
import { Modal } from '../../../ui/Modal/Modal'

export const CategoryList = () => {

  const {handleShowModal} = useContext(FormModalContext)
  const { categories, getCategories, deleteCategory, isLoading, totalPages, error} = useCategory()
  const [contentModal, setContentModal] = useState(null)
  const [refetch, setRefetch] = useState(false)

  const onRefetch = ()=>{
    setRefetch((prev) => !prev)
  }

  const addCategory = ()=>{
    setContentModal(
      <CategoryForm title={'Agregar Categoria'} onRefetch={onRefetch}/>
    )
    handleShowModal()
  }

  const editCategory = (category) =>{
    setContentModal(
      <CategoryForm title={"Editar Categoria"} onRefetch={onRefetch} category={category}/>
    )
    handleShowModal()
  }
  return (
    <main className='dashboard-list-container'>
      <header className='dashboard-header'> Categorias</header>
      <button className='button button-primary' onClick={()=> addCategory()}>Agregar Categoria</button>
      <section className='dashboard-list-item-section'>
        <PaginateItems
        fetchData={getCategories}
        isLoading={isLoading}
        data={categories?.data}
        totalPages={totalPages}
        deleteItem={deleteCategory}
        error={error}
        editItem={editCategory}
        refetch={refetch}
        onRefetch={setRefetch}
        />
      </section>
      <Modal>
        {contentModal}
      </Modal>
    </main>
  )
}
