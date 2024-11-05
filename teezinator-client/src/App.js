import { NextUIProvider } from "@nextui-org/react";
import { Outlet, Route, Routes, useNavigate } from "react-router-dom";
import Login from "./components/auth/Login";
import AuthWrapper from "./components/common/AuthWrapper";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Feed from "./components/feed/Feed";
import NavbarComp from "./components/common/Navbar";
import CreateEntry from "./components/entry/CreateEntry";
import Leaderboard from "./components/leaderboard/Leaderboard";
import Stats from "./components/stats/Stats";
import Profile from "./components/profile/Profile";
import Settings from "./components/settings/Settings";
import CreateVibesEntry from "./components/summervibes/CreateVibesEntry";
import Register from "./components/auth/Register";

function App() {
  const nav = useNavigate();

  const ProtectedRoutes = () => {
    return (
      <AuthWrapper>
        <>
          <NavbarComp />
          <Outlet />
        </>
      </AuthWrapper>
    );
  };

  return (
    <NextUIProvider navigate={nav}>
      <main className="dark text-foreground bg-gray-950 min-h-screen w-screen">
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          <Route element={<ProtectedRoutes />}>
            <Route path="/" element={<Feed />} />

            <Route path="/entry" element={<CreateEntry />} />

            <Route path="/leaderboard" element={<Leaderboard />} />

            <Route path="/stats" element={<Stats />} />

            <Route path="/profile" element={<Profile />} />

            <Route path="/settings" element={<Settings />} />

            <Route path="/vibes" element={<CreateVibesEntry />} />
          </Route>
        </Routes>
        <ToastContainer
          className="mt-2"
          autoClose={3000}
          pauseOnHover={false}
          draggable={true}
          theme="dark"
          closeOnClick
        />
      </main>
    </NextUIProvider>
  );
}

export default App;
