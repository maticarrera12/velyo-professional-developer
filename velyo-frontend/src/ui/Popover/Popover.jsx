import { Popover as PopoverAntd } from 'antd';

import React, { useState } from 'react'

export const Popover = ({children, content}) => {

    const [open, setOpen] = useState(false)

    const hide = () => {
        setOpen(false)
    }

    const handleOpenChange = (newOpen) =>{
        setOpen(newOpen)
    }
  return (
    <PopoverAntd content={
        <div style={{
            width:"200px",
            display:"flex",
            flexDirection:"column",
            gap:"1rem"
        }}>
            {content}
            <a onClick={hide}>Cerrar filtros</a>
        </div>
    }
    trigger="click"
    open={open}
    onOpenChange={handleOpenChange}
    placement='bottomLeft'
    >
        {children}
    </PopoverAntd>
  )
}
