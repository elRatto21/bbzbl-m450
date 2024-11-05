import { useState } from "react";
import { Card, CardBody, Input, Button, Image } from "@nextui-org/react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      await axios
        .post(`${window.location.protocol}//${window.location.hostname}:8080/api/auth/login`, {
          username: username,
          password: password,
        }, {
          headers: {
            "Content-Type": "application/json",
          }
        })
        .then((response) => {
          localStorage.setItem("username", username);
          setCookie("token", response.data.token, 5);
          navigate("/");
        });
    } catch (err) {
      setMessage("Failed to login");
    }
  };

  const setCookie = (name, value, days) => {
    const date = new Date();
    date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
    const expires = "expires=" + date.toUTCString();
    document.cookie = name + "=" + value + ";" + expires + ";path=/";
  };

  return (
    <div className="flex w-full h-screen justify-center items-center">
      <Card className="w-[300px]">
        <CardBody className="flex flex-col items-center pt-8">
          <Image src="logo512.png" width={160} />
          <form className="flex flex-col gap-4 w-11/12 mt-6 mb-3" onSubmit={handleLogin}>
            <Input
              required
              label="Username"
              placeholder="Enter your username"
              type="text"
              value={username}
              onValueChange={setUsername}
            />
            <Input
              label="Password"
              placeholder="Enter your password"
              type="password"
              value={password}
              onValueChange={setPassword}
            />
            <Button fullWidth color="primary" type="submit">
              Login
            </Button>
          </form>
          <div>
            <p className="text-red-500">{message}</p>
          </div>
        </CardBody>
      </Card>
    </div>
  );
};

export default Login;
