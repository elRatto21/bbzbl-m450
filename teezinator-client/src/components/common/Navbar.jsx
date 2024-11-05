import {
  Avatar,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger,
  Image,
  Link,
  Navbar,
  NavbarBrand,
  NavbarContent,
  NavbarMenu,
  NavbarMenuItem,
  NavbarMenuToggle,
  Progress,
} from "@nextui-org/react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { useState } from "react";

const NavbarComp = () => {
  const [username, setUsername] = useState(localStorage.getItem("username"));
  const navigate = useNavigate();
  return (
    <>
      <Navbar>
        <NavbarContent as="div" justify="start" className="lg:hidden">
          <Dropdown className="dark text-white" placement="bottom-end">
            <DropdownTrigger>
              <Avatar
                isBordered
                as="button"
                className="transition-transform"
                color="success"
                size="sm"
                name={username}
                src="https://avatars.githubusercontent.com/u/68444900?v=4"
              />
            </DropdownTrigger>
            <DropdownMenu>
              <DropdownItem key="profile" className="h-14 gap-2">
                <p className="font-semibold">Signed in as</p>
                <p className="font-semibold">{username}</p>
              </DropdownItem>
              <DropdownItem
                key="logout"
                className="text-red-500"
                onClick={() => {
                  document.cookie =
                    "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                  localStorage.removeItem("username");
                  navigate("/login");
                  toast.success("Logged out successfully", {
                    position: "top-right",
                    autoClose: 5000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                    theme: "dark",
                  });
                }}
              >
                Log Out
              </DropdownItem>
            </DropdownMenu>
          </Dropdown>
        </NavbarContent>

        <NavbarContent
          as="div"
          justify="start"
          className="hidden lg:flex justify-center w-fit"
        >
          <NavbarBrand>
            <img
              src="logo192.png"
              width={50}
              alt=""
              onClick={() => navigate("/")}
              className="cursor-pointer"
            />
            <p
              className="ml-2 font-bold text-xl text-inherit cursor-pointer"
              onClick={() => navigate("/")}
            >
              Teezinator
            </p>
          </NavbarBrand>
        </NavbarContent>

        <NavbarContent className="md:hidden pr-3" justify="center">
          <NavbarBrand onClick={() => navigate("/")}>
            <p className="ml-2 font-bold text-xl text-inherit">Teezinator</p>
          </NavbarBrand>
        </NavbarContent>

        <NavbarContent className="md:hidden" justify="end">
          <NavbarMenuToggle />
        </NavbarContent>

        <NavbarContent
          className="hidden md:flex gap-6 text-xl font-semibold"
          justify="center"
        >
          <NavbarMenuItem>
            <Link className="w-full text-orange-300 glitter glow" href="/vibes" size="lg">
              Summer Vibes
            </Link>
          </NavbarMenuItem>
          <NavbarMenuItem>
            <Link className="w-full text-white" href="/" size="lg">
              Feed
            </Link>
          </NavbarMenuItem>
          <NavbarMenuItem>
            <Link className="w-full text-white" href="/entry" size="lg">
              Create Entry
            </Link>
          </NavbarMenuItem>
          <NavbarMenuItem>
            <Link className="w-full text-white" href="/leaderboard" size="lg">
              Leaderboard
            </Link>
          </NavbarMenuItem>
          <Dropdown className="dark text-white" placement="bottom-end">
            <DropdownTrigger>
              <Avatar
                isBordered
                as="button"
                className="transition-transform"
                color="success"
                size="sm"
                name={username}
                src="https://avatars.githubusercontent.com/u/68444900?v=4"
              />
            </DropdownTrigger>
            <DropdownMenu>
              <DropdownItem key="profile" className="h-14 gap-2">
                <p className="font-semibold">Signed in as</p>
                <p className="font-semibold">{username}</p>
              </DropdownItem>
              <DropdownItem
                key="logout"
                className="text-red-500"
                onClick={() => {
                  document.cookie =
                    "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                  localStorage.removeItem("username");
                  navigate("/login");
                  toast.success("Logged out successfully", {
                    position: "top-right",
                    autoClose: 5000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                    theme: "dark",
                  });
                }}
              >
                Log Out
              </DropdownItem>
            </DropdownMenu>
          </Dropdown>
        </NavbarContent>

        <NavbarMenu className="dark gap-6 pt-6 items-end">
        <NavbarMenuItem>
            <Link className="w-full glitter glow text-orange-300" href="/vibes" size="lg">
              Summer Vibes
            </Link>
          </NavbarMenuItem>
          <NavbarMenuItem>
            <Link className="w-full text-white" href="/" size="lg">
              Feed
            </Link>
          </NavbarMenuItem>
          <NavbarMenuItem>
            <Link className="w-full text-white" href="/entry" size="lg">
              Create Entry
            </Link>
          </NavbarMenuItem>
          <NavbarMenuItem>
            <Link className="w-full text-white" href="/leaderboard" size="lg">
              Leaderboard
            </Link>
          </NavbarMenuItem>
        </NavbarMenu>
      </Navbar>
      <div className="h-[5px]">
        <Progress
          size="sm"
          isIndeterminate
          aria-label="Loading..."
          id="loading-progress"
          className="hidden"
        />
      </div>
    </>
  );
};

export default NavbarComp;
