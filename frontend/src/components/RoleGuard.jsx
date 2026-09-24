import { Navigate, useLocation } from 'react-router-dom'
import { useIsAuthenticated } from '@azure/msal-react'
import { useAuth } from '../hooks/useAuth'

export function RequireAuth({ children }) {
  const { token } = useAuth()
  const isAuthenticatedMsal = useIsAuthenticated()
  const location = useLocation()
  
  if (!token && !isAuthenticatedMsal) {
    return <Navigate to="/login" state={{ from: location }} replace />
  }
  
  return children
}

export function RequireAdmin({ children }) {
  const { role } = useAuth()
  if (role !== 'administrador') return <div>No autorizado (solo administrador).</div>
  return children
}
