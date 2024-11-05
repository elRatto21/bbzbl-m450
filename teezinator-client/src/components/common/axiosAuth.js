import axios from "axios";

const axiosAuth = axios.create({
  baseURL: `${window.location.protocol}//${window.location.hostname}:8080/api`,
});

axiosAuth.interceptors.request.use(
  (config) => {
    const cookies = document.cookie.split(";").map((cookie) => cookie.trim());
    const tokenCookie = cookies.find((cookie) => cookie.startsWith("token="));
    const token = tokenCookie ? tokenCookie.split("=")[1] : null;

    if (token) {
      config.headers["Authorization"] = "Bearer " + token;
      config.headers["Content-Type"] = "application/json";
    }
    return config;
  },
  (error) => {
    Promise.reject(error);
  }
);

axiosAuth.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    const { status } = error.response;
    if (status === 401) {
      document.cookie =
        "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export default axiosAuth;
