import { notification } from "antd";
import { NotificationContext } from "./NotificationContext";


export const NotificationProvider = ({ children }) => {
    const [toaster, contextHolder] = notification.useNotification();

    return (
        <NotificationContext.Provider value={{toaster}}>
            {contextHolder}
            {children}
        </NotificationContext.Provider>
    )
}