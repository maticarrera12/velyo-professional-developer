import { useEffect, useState } from "react"
import { Link, useLocation } from "react-router-dom"
import { ItemSkeleton } from "../ItemSkeleton/ItemSkeleton"
import { Empty } from "antd"
import { ItemList } from "../ItemList/ItemList"
import { Pagination, Stack } from "@mui/material"
import { ConfirmAction } from "../ConfirmAction/ConfirmAction"



export const PaginateItems = ({fetchData, isLoading, data, deleteItem, totalPages, error, editItem, refetch, onRefetch}) => {

    const [currentPage, setCurrentPage] = useState(1)
    const [showModal, setShowModal] = useState(false)
    const [deleteItemId, setDeleteItemId] = useState(null)
    const location = useLocation()
    console.log('DATA QUE LLEGA A PaginateItems:', data);

    useEffect(()=>{
        fetchData(currentPage - 1)
    }, [currentPage, refetch])


    const handlePageChange =(event, value) =>{
        setCurrentPage(value)
    }

    const openCloseModalDelete = () =>{
        setShowModal((prev)=> !prev)
    }
  return (
    <>
    {isLoading ?(
        Array.from({ length: 5}).map((_, index)=>(
            <ItemSkeleton key={index}/>
        ))
    ) : error ? (
        <Empty description="¡Ups! Ha ocurrido un error."/>
    ) : data?.length === 0 ? (
        <Empty description="¡Ups! Parece que no hay nada cargado."/> 
    ) : (
        data?.map((item, index)=> (
            // <Link key={index} to={`${location.pathname}/${item.id}`}>
            <div key={index}>
                 <ItemList
                data={item}
                openCloseModal={openCloseModalDelete}
                setDeleteItemId={setDeleteItemId}
                editItem={editItem}
                />
            </div>   
            
           
            // {/* </Link> */}
        ))
    )
}

{
    totalPages > 1 && (
        <Stack spacing={2} alignItems="center" sx={{mt:4}}>
            <Pagination
              count={totalPages}
              page={currentPage}
              onChange={handlePageChange}
              shape="rounded"
              sx={{
                "& .MuiPaginationItem-root": {
                  color: "#DF1174",
                  borderColor: "#DF1174",
                },
                "& .Mui-selected": {
                  backgroundColor: "#DF1174",
                  color: "#fff",
                  "&:hover": {
                    backgroundColor: "#c01066",
                  },
                },
              }}
            />
        </Stack>
    )
}

<ConfirmAction
showModal={showModal}
openCloseModal={openCloseModalDelete}
deleteItem={()=> deleteItem(deleteItemId)}
onReFetch={onRefetch}
/>
    </>
  )
}

