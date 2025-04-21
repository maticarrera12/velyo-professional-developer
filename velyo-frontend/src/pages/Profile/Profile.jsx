import { useContext, useState } from "react"
import "./Profile.css"
import { useUser } from "../../hooks/useUser"
import { useAuth } from "../../auth/hook/useAuth"
import { NotificationContext } from "../../context/NotificationContext"
import { useFormik } from "formik"
import * as Yup from 'yup';
import { CloseOutlined, EditOutlined, Loading3QuartersOutlined, SaveFilled } from "@ant-design/icons"

export const Profile = () => {

    const [edit, setEdit] = useState(false)
    const {updateName, isLoading} = useUser()
    const {user, updateUser} = useAuth()
    const {toaster} = useContext(NotificationContext)

    const formik = useFormik({
        initialValues:{
            firstname: user.firstname,
            lastname: user.lastname
        },
        validationSchema: Yup.object({
            firstname: Yup.string().required('El nombre es obligatorio'),
            lastname: Yup.string().required('El apellido es obligatorio')
        }),
        validateOnChange: true,
        onSubmit: async (values) => {
            try {
                const response = await updateName(values)
                toaster['success']({
                    message: 'Nombre actualizado correctamente',
                    description: 'El nombre se ha actualizado correctamente',
                    duration: 3
                })
                updateUser(response)
                setEdit(false)
            } catch (error) {
                toaster['error']({
                    message:'No se pudo actualizar el nombre',
                    description: error.message,
                    duration: 3
                })
                return
            }
        }
    })
  return (
    <main className="profile-container">
        <button type="button" className="button button-outline" onClick={()=> setEdit(!edit)}>{edit? <><CloseOutlined/>Cancelar</> : <><EditOutlined/>Editar</>}</button>
        <section className="profile-info">
            <figure>
                {
                    user.avatar ? 
                    <img src={user.avatar} alt="Avatar" /> :
                    <p>{user.firstname[0]}{user.lastname[0]}</p>
                }
            </figure>
            <h3>{user.email}</h3>
            <p>{user.role}</p>
        </section>
        <hr className="separator"/>
        <form className="profile-form" onSubmit={formik.handleSubmit}>
            <div className="form-container">
 <label className="form-label">Nombre
                <input type="text" name="firstname" value={formik.values.firstname} onChange={formik.handleChange} disabled={!edit}/>
            </label>
            {
                (formik.errors.firstname && formik.touched.firstname) && <label className="label-error">{formik.errors.firstname}</label>
            }
            </div>
            <div className="form-container">
 <label className="form-label">Apellido
                <input type="text" name="lastname" value={formik.values.lastname} onChange={formik.handleChange} disabled={!edit}/>
            </label>
            {
                (formik.errors.lastname && formik.touched.lastname) && <label className="label-error">{formik.errors.lastname}</label>
            }
            </div>
           <button type="submit" className="button button-primary" disabled={!edit || isLoading}>
            {
                isLoading ? <Loading3QuartersOutlined className="spin-animation"/> : <SaveFilled/>
            }
            Actualizar nombre
           </button>
        </form>
    </main>
  )
}
