import { Skeleton } from 'antd'
import React from 'react'

export const AccommodationSkeleton = () => {
  return (
    <Skeleton active title={{width: '100%', style: {height:"200px"}}}
    paragraph={{rows:0}}/>
  )
}
