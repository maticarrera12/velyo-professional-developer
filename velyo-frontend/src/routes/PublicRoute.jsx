import { Navigate } from "react-router-dom";
import { useAuth } from "../auth/hook/useAuth";

export const PublicRoute = ({ children }) => {

    const { user } = useAuth();

    if (user) {
        return <Navigate to="/" />;
    }

    return children;
}