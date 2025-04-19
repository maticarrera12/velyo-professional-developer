import React, { useContext, useState } from 'react'
import { useUser } from '../../../hooks/useUser'
import { FormModalContext } from '../../../context/FormModalContext'
import { UserForm } from '../../components/UserForm/UserForm'
import { PaginateItems } from '../../../ui/PaginateItems/PaginateItems'
import { Modal } from '../../../ui/Modal/Modal'



export const UserList = () => {

  const {users, getUsers, deleteUser, isLoading, totalPages, error} = useUser()
  const {handleShowModal} = useContext(FormModalContext)
  const [contentModal, setContentModal] = useState(null)
  const [refetch, setRefetch] = useState(false)

  const onRefetch = () =>{
    setRefetch((prev)=> !prev)
  }

  const addUser=()=>{
    setContentModal(
      <UserForm onRefetch={onRefetch}/>
    )
    handleShowModal()
  }

  const editUser = (user) =>{
    setContentModal(
      <UserForm user={user} onRefetch={onRefetch}/>
    )
    handleShowModal()
  }


  return (
    <main className='dashboard-list-container'>
<header className='dashboard-header'>
  <h1>Usuarios</h1>
</header>
<button className='button button-primary' onClick={addUser}>Agregar usuario</button>

<section className='dashboard-list-item-section'>
  <PaginateItems 
  fetchData={getUsers}
  isLoading={isLoading}
  data={users?.data}
  totalPages={totalPages}
  deleteItem={deleteUser}
  error={error}
  editItem={editUser}
  refetch={refetch}
  onRefetch={onRefetch}
  />
</section>
<Modal>
  {contentModal}
  </Modal> 
    </main>
  )
}
