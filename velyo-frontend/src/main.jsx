import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { ConfigProvider } from 'antd'

createRoot(document.getElementById('root')).render(
     <ConfigProvider
      theme={{
        components: {
          Select: {
            colorBorder: '#a8aebd',
          },
        },
        token: {
          colorPrimary: '#DF1174',
        }
      }}
    >
      <App />
      </ConfigProvider>  
  
)
