import { useEffect, useMemo, useState, useCallback, useRef } from "react";
import { useMsal } from "@azure/msal-react";
import { api, setAuthToken } from "../lib/api";
import { AuthContext } from "./authContextInstance";
import { jwtDecode } from "jwt-decode";
import { toast } from "sonner";
import { msalRedirectResponse } from "../main";

export default function AuthProvider({ children }) {
  const { instance } = useMsal();
  const [token, setToken] = useState(
    () => localStorage.getItem("token") || null
  );
  const [user, setUser] = useState(() => {
    const u = localStorage.getItem("user");
    return u ? JSON.parse(u) : null;
  });
  const [role, setRole] = useState(
    () => localStorage.getItem("role") || "administrador"
  );
  const [loading, setLoading] = useState(false);
  const redirectProcessedRef = useRef(false);

  useEffect(() => {
    setAuthToken(token);
  }, [token]);

  // Procesar el resultado de loginRedirect UNA SOLA VEZ al montar.
  // msalRedirectResponse viene de main.jsx, que llamó handleRedirectPromise()
  // antes de que React se renderizara, por lo que nunca se repite.
  useEffect(() => {
    if (redirectProcessedRef.current) return;
    if (!msalRedirectResponse?.account) return;

    redirectProcessedRef.current = true;
    const newToken = msalRedirectResponse.accessToken;
    if (!newToken) {
      console.error("No se obtuvo accessToken de Microsoft Entra ID.");
      toast.error("Error de autenticación: No se recibió token de acceso.");
      return;
    }

    const account = msalRedirectResponse.account;
    const userData = {
      nombre_usuario: account.username || account.name || "Usuario Microsoft",
      email: account.username,
      nombre: account.name,
      tenantId: account.tenantId,
      sub: account.localAccountId || account.homeAccountId,
      provider: "entra-id",
    };

    setToken(newToken);
    setUser(userData);
    localStorage.setItem("token", newToken);
    localStorage.setItem("user", JSON.stringify(userData));

    const r = localStorage.getItem("role") || "administrador";
    setRole(r);
    localStorage.setItem("role", r);

    toast.success("Inicio de sesión con Microsoft exitoso");
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const login = useCallback(async ({ nombre_usuario, password }) => {
    setLoading(true);
    try {
      const { data } = await api.post("/autenticacion/login", {
        nombre_usuario,
        password,
      });
      const newToken = data?.token;
      setToken(newToken);
      localStorage.setItem("token", newToken);
      setAuthToken(newToken);

      let decodedToken = null;
      try {
        decodedToken = jwtDecode(newToken);
      } catch (error) {
        console.error("Error al decodificar JWT:", error);
      }

      const userData = {
        nombre_usuario,
        ...(decodedToken || {}),
      };

      setUser(userData);
      localStorage.setItem("user", JSON.stringify(userData));
      const r = localStorage.getItem("role") || "administrador";
      setRole(r);
      localStorage.setItem("role", r);
    } finally {
      setLoading(false);
    }
  }, []);

  // loginWithMsal se puede seguir usando si algo externo lo necesita,
  // pero el flujo principal de loginRedirect ya va por el useEffect de arriba.
  const loginWithMsal = useCallback((msalResponse) => {
    const newToken = msalResponse?.accessToken;
    if (!newToken) {
      console.error("No se obtuvo accessToken de Microsoft Entra ID.");
      toast.error("Error de autenticación: No se recibió token de acceso.");
      return;
    }
    const account = msalResponse?.account || msalResponse;
    const userData = {
      nombre_usuario: account?.username || account?.name || "Usuario Microsoft",
      email: account?.username,
      nombre: account?.name,
      tenantId: account?.tenantId,
      sub: account?.localAccountId || account?.homeAccountId,
      provider: "entra-id",
    };

    setToken(newToken);
    setUser(userData);
    localStorage.setItem("token", newToken);
    localStorage.setItem("user", JSON.stringify(userData));

    const r = localStorage.getItem("role") || "administrador";
    setRole(r);
    localStorage.setItem("role", r);
  }, []);

  const logout = useCallback(() => {
    // 1. Limpiar estado local y localStorage
    setToken(null);
    setUser(null);
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    localStorage.removeItem("role");

    // 2. Limpiar la caché de MSAL localmente (sin contactar a Microsoft).
    //    clearCache() elimina todas las cuentas y tokens de la sesión del browser
    //    sin redirigir a login.microsoftonline.com.
    //    Después navegamos a /login directamente con window.location.
    if (instance) {
      instance.clearCache().then(() => {
        window.location.replace("/login");
      }).catch((err) => {
        console.error("Error limpiando caché de MSAL:", err);
        window.location.replace("/login");
      });
    } else {
      window.location.replace("/login");
    }
  }, [instance]);

  const value = useMemo(
    () => ({ token, user, role, setRole, loading, login, loginWithMsal, logout }),
    [token, user, role, loading, login, loginWithMsal, logout]
  );
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
