import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import { CssBaseline, ThemeProvider } from '@mui/material'
import { PublicClientApplication } from '@azure/msal-browser'
import { MsalProvider } from '@azure/msal-react'
import { msalConfig } from './authConfig'
import AuthProvider from './context/AuthContext'
import './index.css'
import App from './App.jsx'
import theme from './theme'

const msalInstance = new PublicClientApplication(msalConfig);

await msalInstance.initialize();

const redirectResponse = await msalInstance.handleRedirectPromise();
export const msalRedirectResponse = redirectResponse;

if (redirectResponse?.account) {
  msalInstance.setActiveAccount(redirectResponse.account);
} else if (!msalInstance.getActiveAccount() && msalInstance.getAllAccounts().length > 0) {
  msalInstance.setActiveAccount(msalInstance.getAllAccounts()[0]);
}

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <MsalProvider instance={msalInstance}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <AuthProvider>
          <BrowserRouter>
            <App />
          </BrowserRouter>
        </AuthProvider>
      </ThemeProvider>
    </MsalProvider>
  </StrictMode>,
)
