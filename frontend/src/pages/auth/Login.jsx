import { useForm } from "react-hook-form";
import { useNavigate, Link } from "react-router-dom";
import { useMsal } from "@azure/msal-react";
import { InteractionStatus } from "@azure/msal-browser";
import { loginRequest } from "../../authConfig";
import { useAuth } from "../../hooks/useAuth";
import bgRegistro from "../../assets/bg-registro.jpg";
import { toast } from "sonner";
import Logo from "../../assets/logo.svg";
import LogoDuoc from "../../assets/logo-duoc.svg";
import HelpIcon from "@mui/icons-material/Help";
import {
  Box,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Typography,
} from "@mui/material";
import { useState, useEffect } from "react";

export default function Login() {
  const { register, handleSubmit } = useForm({
    defaultValues: { nombre_usuario: "", password: "" },
  });
  const { login, loading, user } = useAuth();
  const { instance, inProgress } = useMsal();
  const [openDialog, setOpenDialog] = useState(false);
  const navigate = useNavigate();

  // Si el usuario ya está autenticado (sesión persistente o redirect ya procesado en main.jsx),
  // redirigir a /home directamente sin mostrar notificación extra.
  useEffect(() => {
    if (user) {
      navigate("/home", { replace: true });
    }
  }, [user, navigate]);

  const onSubmit = async (values) => {
    try {
      await login(values);
      toast.success("Sesión iniciada");
      navigate("/home", { replace: true });
    } catch {
      toast.error("Credenciales inválidas");
    }
  };

  const handleMicrosoftLogin = async () => {
    if (inProgress !== InteractionStatus.None && inProgress !== "none") {
      return;
    }
    try {
      await instance.loginRedirect(loginRequest);
    } catch (error) {
      console.error("Error al iniciar sesión con Microsoft:", error);
      toast.error("Error al iniciar sesión con Microsoft");
    }
  };

  const handleOpenDialog = () => {
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
  };

  return (
    <div className="min-h-screen w-full flex flex-row items-center justify-cente relative overflow-hidden p-0">
      <div
        className="fixed inset-0 w-full h-full bg-cover bg-center bg-no-repeat -z-10"
        style={{ backgroundImage: `url(${bgRegistro})` }}
      ></div>

      <div className="w-6/12 min-h-screen relative z-10 bg-white flex items-center justify-center shadow-lg">
        <div className="p-8 w-full max-w-md">
          <Box
            sx={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              mb: 8,
            }}
          >
            <img src={Logo} alt="Logo" width={200} />
          </Box>
          <h2 className="font-bold text-4xl text-center mb-6 text-primary">
            Iniciar Sesión
          </h2>
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
            <div>
              <label
                htmlFor="nombre_usuario"
                className="block text-sm font-medium text-gray-700 mb-2"
              >
                Nombre de usuario
              </label>
              <input
                id="nombre_usuario"
                type="text"
                data-testid="input-nombre-usuario"
                {...register("nombre_usuario", { required: true })}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent hover:border-secondary transition-colors"
              />
            </div>
            <div>
              <label
                htmlFor="password"
                className="block text-sm font-medium text-gray-700 mb-2"
              >
                Contraseña
              </label>
              <input
                id="password"
                type="password"
                data-testid="input-password"
                {...register("password", { required: true })}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent hover:border-secondary transition-colors"
              />
            </div>
            <button
              type="submit"
              disabled={loading}
              data-testid="button-login"
              className="w-full text-white mt-4 py-3 text-lg font-semibold bg-secondary rounded-lg hover:bg-primary hover:shadow-lg hover:-translate-y-0.5 disabled:bg-gray-500 disabled:text-gray-400 disabled:cursor-not-allowed transition-all duration-300"
            >
              {loading ? "Ingresando..." : "Entrar"}
            </button>

            <div className="relative my-4">
              <div className="absolute inset-0 flex items-center">
                <div className="w-full border-t border-gray-300" />
              </div>
              <div className="relative flex justify-center text-sm">
                <span className="bg-white px-2 text-gray-500">O</span>
              </div>
            </div>

            <button
              type="button"
              onClick={handleMicrosoftLogin}
              data-testid="button-msal-login"
              className="w-full flex items-center justify-center gap-2 text-gray-700 py-3 text-lg font-semibold bg-white border border-gray-300 rounded-lg hover:bg-gray-50 hover:shadow-lg hover:-translate-y-0.5 transition-all duration-300"
            >
              <svg className="w-5 h-5" viewBox="0 0 23 23">
                <path fill="#f35325" d="M1 1h10v10H1z" />
                <path fill="#81bc06" d="M12 1h10v10H1z" />
                <path fill="#05a6f0" d="M1 12h10v10H1z" />
                <path fill="#ffba08" d="M12 12h10v10H1z" />
              </svg>
              Iniciar sesión con Microsoft
            </button>

            <p className="text-center mt-4 text-gray-600 text-sm">
              ¿No tienes cuenta?{" "}
              <Link
                to="/registro"
                data-testid="link-registro"
                className="text-primary font-semibold no-underline hover:text-secondary hover:underline"
              >
                Regístrate
              </Link>
            </p>
          </form>
        </div>
      </div>

      {/* Botón fijo en la parte inferior izquierda */}
      <button
        onClick={handleOpenDialog}
        className="fixed bottom-6 left-6 z-20 px-4 py-2 bg-primary text-white rounded-lg shadow-lg hover:bg-secondary hover:shadow-xl transition-all duration-300 font-medium"
      >
        <HelpIcon className="mr-2 text-white" /> Guía inicial
      </button>

      {/* Dialog con la información */}
      <Dialog
        open={openDialog}
        onClose={handleCloseDialog}
        maxWidth="md"
        fullWidth
      >
        <DialogTitle>
          <Typography
            className="text-primary"
            variant="h5"
            component="div"
            fontWeight="bold"
          >
            Guía Inicial
          </Typography>
        </DialogTitle>
        <DialogContent>
          <Box sx={{ mb: 3 }}>
            <Typography
              variant="h6"
              component="h3"
              gutterBottom
              fontWeight="bold"
            >
              Usuarios predeterminados
            </Typography>
            <TableContainer component={Paper} sx={{ mt: 2, mb: 3 }}>
              <Table>
                <TableHead>
                  <TableRow sx={{ backgroundColor: "#f5f5f5" }}>
                    <TableCell sx={{ fontWeight: "bold" }}>Usuario</TableCell>
                    <TableCell sx={{ fontWeight: "bold" }}>
                      Contraseña
                    </TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  <TableRow>
                    <TableCell>admin</TableCell>
                    <TableCell>admin</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>editor</TableCell>
                    <TableCell>editor</TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </TableContainer>
          </Box>

          <div className="flex flex-row gap-4">
            <div className="w-1/6 items-center justify-center text-end">
              <img src={LogoDuoc} alt="Logo" width={200} className="mx-auto" />
            </div>
            <div className="w-5/6 pl-10">
              <Box sx={{ mb: 3 }}>
                <Typography
                  variant="h6"
                  component="h3"
                  gutterBottom
                  fontWeight="bold"
                >
                  Acerca de este proyecto
                </Typography>
                <p className="text-sm mb-4 text-justify ">
                  Esta aplicación fue desarrollada para DUOC UC en el año 2025,
                  en el marco de la asignatura{" "}
                  <strong className="text-primary">
                    ISY1102 - Calidad y Seguridad en el Desarrollo de Software
                  </strong>
                  . Su objetivo es presentar a los estudiantes un caso realista
                  de desarrollo de software, que incorpore las características
                  propias de un sistema en producción y permita evaluar el
                  cumplimiento de estándares de calidad y seguridad.
                </p>
                <p className="text-sm">
                  El software contiene errores introducidos de manera
                  intencional con fines académicos, por lo que{" "}
                  <strong className="text-primary">
                    no debe ser utilizado en entornos de producción
                  </strong>
                  .
                </p>
              </Box>
              <Box>
                <Typography
                  variant="h6"
                  component="h3"
                  gutterBottom
                  fontWeight="bold"
                >
                  Docentes:
                </Typography>
                <ul className="text-sm m-0 pl-5 list-disc">
                  <li>Codiseñador Claudio Gonzalez Pauzoca</li>
                  <li>Codiseñador Luis Ponce Sánchez</li>
                </ul>
              </Box>
            </div>
          </div>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={handleCloseDialog}
            variant="contained"
            color="primary"
          >
            Cerrar
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
