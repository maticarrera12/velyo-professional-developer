import { Navbar } from "../../ui/Navbar/Navbar";
import { FormModalProvider } from "../../context/FormModalProvider";
import { Footer } from "../../ui/Footer/Footer";
import './UserLayout.css'
export const UserLayout = ({children}) => {
  return (
    <main className="user-layout-container">
      <Navbar />
      <FormModalProvider>{children}</FormModalProvider>
    <Footer/>
    </main>
  );
};
