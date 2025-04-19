
import React, { useEffect, useState } from "react";
import {
  AppstoreOutlined,
  CalendarOutlined,
  HomeOutlined,
  TagsOutlined,
  UserOutlined,
} from "@ant-design/icons";
import { Skeleton, Empty, Avatar } from "antd";
import { useAuth } from "../../../auth/hook/useAuth";
import api from "../../../api";
import "./Dashboard.css";

export const Dashboard = () => {
  const { user, token } = useAuth();
  const [info, setInfo] = useState({
    isLoading: null,
    data: null,
    error: null,
  });

  useEffect(() => {
    const getInfo = async () => {
      setInfo({ isLoading: true, data: null, error: null });
      try {
        const response = await api.get("/users/dashboard", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setInfo({ isLoading: false, data: response.data, error: null });
      } catch{
        setInfo({
          loading: false,
          data: null,
          error: "No se pudieron obtener los datos",
        });
      }
    };
    getInfo();
  }, []);

  return (
    <main className="dashboard-main">
      <section className="dashboard-section">
        <article className="dashboard-article">
          <p>Velyo</p>
          <h2>Panel de Administración</h2>
        </article>
        {info.isLoading ? (
          <Skeleton
            active
            title={{ width: "100%", style: { height: "320px", marginTop: "1rem" } }}
            paragraph={{ rows: 0 }}
          />
        ) : info.error ? (
          <Empty description={info.error} />
        ) : (
          <div className="dashboard-card-container">
            <CardDash title="Alojamientos" total={info.data?.totalAccommodations} Icon={<HomeOutlined />} />
            <CardDash title="Características" total={info.data?.totalAmenities} Icon={<AppstoreOutlined />} />
            <CardDash title="Categorías" total={info.data?.totalCategories} Icon={<TagsOutlined />} />
            <CardDash title="Usuarios" total={info.data?.totalUsers} Icon={<UserOutlined />} />
            <CardDash title="Reservas" total={info.data?.totalBookings} Icon={<CalendarOutlined />} />
          </div>
        )}
      </section>

      <section className="dashboard-user-section">
      <Avatar className="avatar" size={64}>
             {user
             ? `${user.firstname?.[0]?.toUpperCase() ?? ""}${
                  user.lastname?.[0]?.toUpperCase() ?? ""
               }`
              : "USER"}
        </Avatar>
        <p className="profile-name">{user?.firstname} {user?.lastname}</p>
        <p>{user?.email}</p>
      </section>
    </main>
  );
};

const CardDash = ({ title, total, Icon }) => {
  return (
    <article className="card-dashboard-container">
      <div className="card-dashboard-icon">{Icon}</div>
      <div className="card-dashboard-content">
        <p>{title}</p>
        <p>{total}</p>
      </div>
    </article>
  );
};
