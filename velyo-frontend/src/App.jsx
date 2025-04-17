import { AuthProvider } from "./auth/context/AuthProvider"
import { NotificationProvider } from "./context/NotificationProvider"
import { Navigation } from "./routes/Navigation"

function App() {


  return (
    <AuthProvider>
      <NotificationProvider>
        <Navigation/>
      </NotificationProvider>
    </AuthProvider>
    
  )
}

export default App
