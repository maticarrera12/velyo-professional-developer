import "./ItemList.css";

export const ItemList = ({
  data,
  openCloseModal,
  setDeleteItemId,
  editItem,
}) => {
  const { id, name, email, images, image, icon } = data;

  const handleClickDelete = (e) => {
    e.preventDefault();
    setDeleteItemId(id),
    openCloseModal()
  }

  const handleClickEdit = (e) =>{
    e.preventDefault();
    editItem(data)
  }

  const renderMedia = ()=>{
    if (images && images.length > 0) {
        return <img src={images[0]} alt="Vista previa" width={80} height={80} />;
    } else if (image) {
        return <img src={image} alt="Vista previa" width={80} height={80} />;
    } else if (icon) {
        return <img src={icon} alt="Icono" width={80} height={80} />;
    }
    return null;
  }

  return (
    <article className="item-list-container">
        <div className="item-list-info">
            {(images || image || icon) && (
                <div className="item-list-image">
                    {renderMedia()}
                </div>
            )}
        </div>
        <div className="item-list-info">
            <p><strong>Id</strong></p>
            <p>{id}</p>
        </div>

        {
            name ? (
                <div className="item-list-info">
            <p><strong>Nombre</strong></p>
            <p>{name}</p>
        </div>
            ) : (
                <div className="item-list-info">
                    <p><strong>Email</strong></p>
                    <p>{email}</p>
                </div>
            )
        }
        
        <div className='item-list-actions'>
                <button onClick={handleClickEdit}>
                    <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 30 30" fill="none">
                        <path d="M6.25 26.25C5.5625 26.25 4.97396 26.0052 4.48438 25.5156C3.99479 25.026 3.75 24.4375 3.75 23.75V6.25C3.75 5.5625 3.99479 4.97396 4.48438 4.48438C4.97396 3.99479 5.5625 3.75 6.25 3.75H17.4063L14.9063 6.25H6.25V23.75H23.75V15.0625L26.25 12.5625V23.75C26.25 24.4375 26.0052 25.026 25.5156 25.5156C25.026 26.0052 24.4375 26.25 23.75 26.25H6.25ZM11.25 18.75V13.4375L22.7188 1.96875C22.9688 1.71875 23.25 1.53125 23.5625 1.40625C23.875 1.28125 24.1875 1.21875 24.5 1.21875C24.8333 1.21875 25.151 1.28125 25.4531 1.40625C25.7552 1.53125 26.0313 1.71875 26.2813 1.96875L28.0313 3.75C28.2604 4 28.4375 4.27604 28.5625 4.57813C28.6875 4.88021 28.75 5.1875 28.75 5.5C28.75 5.8125 28.6927 6.11979 28.5781 6.42188C28.4635 6.72396 28.2813 7 28.0313 7.25L16.5625 18.75H11.25ZM13.75 16.25H15.5L22.75 9L21.875 8.125L20.9688 7.25L13.75 14.4688V16.25Z" fill="#EFF0F3" />
                    </svg>
                </button>
                <button onClick={handleClickDelete}>
                    <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 30 30" fill="none">
                        <path d="M8.75 26.25C8.0625 26.25 7.47396 26.0052 6.98437 25.5156C6.49479 25.026 6.25 24.4375 6.25 23.75V7.5H5V5H11.25V3.75H18.75V5H25V7.5H23.75V23.75C23.75 24.4375 23.5052 25.026 23.0156 25.5156C22.526 26.0052 21.9375 26.25 21.25 26.25H8.75ZM21.25 7.5H8.75V23.75H21.25V7.5ZM11.25 21.25H13.75V10H11.25V21.25ZM16.25 21.25H18.75V10H16.25V21.25Z" fill="#E8EAED" />
                    </svg>
                </button>
            </div>

      
    </article>
  );
};
