import { BooleanParam, useQueryParam } from 'use-query-params'
import './BookingsUser.css'
import { useBooking } from '../../hooks/useBooking';
import { useEffect, useState } from 'react';
import { format } from 'date-fns';
import { Empty, Skeleton } from 'antd';
import { BookingCard } from '../../components/BookingCard/BookingCard';
import { CardEmpty } from '../../ui/CardEmpty/CardEmpty';
import { Modal } from '../../ui/Modal/Modal';

export const BookingsUser = () => {

    const [query, setQuery] = useQueryParam('history', BooleanParam);
    const { isLoading, success, error, bookings, getBookingsByUser} = useBooking()

    const [onRefetch, setOnRefetch] = useState(false)

    useEffect(()=>{
        query ? 
        getBookingsByUser()
        :
        getBookingsByUser(format(new Date(), 'yyyy-MM-dd'))
    }, [query, onRefetch])

    const onRefetchData = () => {
        setOnRefetch(!onRefetch);
    }

    const handleViewHistory = () => {
        setQuery(true);
    }

    const handleViewCurrent = () => {
        setQuery(null);
    }
  return (
    <main className='bookings-user-container'>
        <h2>Mis reservas</h2>
        {
            query ? 
            <p className='bookings-user-container-option' onClick={handleViewCurrent}>Ver reservas actuales</p>
            :
            <p className='bookings-user-container-option' onClick={handleViewHistory}>Ver historial</p>
        }
        <hr className="separator" />
        {
            isLoading ?
            <Skeleton
            active
                        title={{ width: '100%', style: { height: "250px" } }}
                        paragraph={{ rows: 0 }} />
            :
            error ? 
            <Empty description="No se pudieron cargar las reservas"/>
            :
            (success && bookings.length > 0) ? 
            <section className='booking-user-list'>
                {
                    bookings.map((booking, index)=>{
                        return(
                            <BookingCard booking={booking} key={index} onRefetch={onRefetchData}/>
                        )
                    })
                }
            </section>
            :<CardEmpty
            title="No tenes reservas hechas!"
            description="Busca alojamientos y comenza a planificar tu viaje"/>
        }

        <hr className='separator'/>
        <Modal/>
    </main>
  )
}


