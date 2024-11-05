import React from "react";
import { Navigate } from "react-router-dom";

export const isTokenValid = (token) => {
  if (!token) return false;

  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split("")
        .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join("")
    );
    const { exp } = JSON.parse(jsonPayload);
    const currentTime = Date.now() / 1000;
    return exp > currentTime;
  } catch (error) {
    return false;
  }
};

const AuthWrapper = ({ children }) => {
  const cookies = document.cookie.split(";").map((cookie) => cookie.trim());
  const tokenCookie = cookies.find((cookie) =>
    cookie.startsWith("token=")
  );
  const token = tokenCookie ? tokenCookie.split("=")[1] : null;

  if (!isTokenValid(token)) {
    return <Navigate to="/login" />;
  }

  return children;
};

export default AuthWrapper;
