import {
  Card,
  CardBody,
  CardHeader,
  Image,
  Avatar,
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
  Tooltip,
  Button,
} from "@nextui-org/react";
import { useState } from "react";
import axiosAuth from "../common/axiosAuth";

const FeedEntry = ({ entry }) => {
  const [showImage, setShowImage] = useState(false);
  const [image, setImage] = useState(null);

  const formatDate = (date) =>
    `${date.getDate().toString().padStart(2, "0")}.${(date.getMonth() + 1)
      .toString()
      .padStart(2, "0")}.${date.getFullYear()}`;

  const formatDateTime = (dateString) => {
    const date = new Date(dateString);
    return `${date.getDate().toString().padStart(2, "0")}.${(
      date.getMonth() + 1
    )
      .toString()
      .padStart(2, "0")}.${date.getFullYear()} ${date
      .getHours()
      .toString()
      .padStart(2, "0")}:${date.getMinutes().toString().padStart(2, "0")}`;
  };

  function calcTime(dateString) {
    const date = new Date(dateString);
    const now = new Date();
    const diffInSeconds = Math.round((now - date) / 1000);
    const diffInMinutes = Math.round(diffInSeconds / 60);
    const diffInHours = Math.round(diffInMinutes / 60);
    const diffInDays = Math.round(diffInHours / 24);

    if (diffInSeconds < 60) {
      return "Just now";
    } else if (diffInMinutes < 60) {
      return `${diffInMinutes} minutes ago`;
    } else if (diffInHours < 24) {
      return `${diffInHours} hours ago`;
    } else if (diffInDays <= 3) {
      return `${diffInDays} days ago`;
    } else {
      return formatDate(date);
    }
  }

  const handleOpenImage = async () => {
    setShowImage(true);
    await axiosAuth
      .get("/stats/getImageById?imageId=" + entry.image)
      .then((res) => {
        setImage(res.data);
      });
  };

  return (
    <>
      {entry.type === "SUMMERVIBES" ? (
        <Card className="card-glitter">
          <CardHeader className="pb-0 pt-2.5 px-4 flex-col items-start">
            <div className="flex items-center gap-2 mb-2.5">
              <Avatar name={entry.user.charAt(0)} />
              <div className="flex flex-col">
                <p className="text-medium font-bold">{entry.user}</p>
                <Tooltip
                  classNames={{
                    base: "dark",
                    content: "dark text-white",
                  }}
                  showArrow={true}
                  content={formatDateTime(entry.time)}
                  delay={750}
                >
                  <small className="text-gray-200">
                    {calcTime(entry.time)}
                  </small>
                </Tooltip>
              </div>
            </div>
            <h4 className="font-semibold">{entry.tea.name} @ Summer Vibes</h4>
          </CardHeader>
          {entry.preview ? (
            <CardBody className="overflow-visible py-2">
              <div className="h-[200px] overflow-hidden rounded-xl">
                <Image
                  src={entry.preview}
                  alt="cover"
                  width={400}
                  className="object-cover rounded-none h-[200px] w-full hover:scale-110 cursor-pointer"
                  onClick={handleOpenImage}
                />
              </div>
            </CardBody>
          ) : entry.image ? (
            <CardBody>
              <Button
                onClick={handleOpenImage}
                className="w-1/3"
                variant="flat"
              >
                Show Image
              </Button>
            </CardBody>
          ) : null}
        </Card>
      ) : entry.type === "POST" ? (
        <Card className="bg-gray-800">
          <CardHeader className="pb-0 pt-2.5 px-4 flex-col items-start">
            <div className="flex items-center gap-2 mb-2.5">
              <Avatar name={entry.user.charAt(0)} />
              <div className="flex flex-col">
                <p className="text-medium font-bold">{entry.user}</p>
                <Tooltip
                  classNames={{
                    base: "dark",
                    content: "dark text-white",
                  }}
                  showArrow={true}
                  content={formatDateTime(entry.time)}
                  delay={750}
                >
                  <small className="text-default-500">
                    {calcTime(entry.time)}
                  </small>
                </Tooltip>
              </div>
            </div>
          </CardHeader>
          {entry.preview ? (
            <CardBody className="overflow-visible py-2">
              <div className="h-[200px] overflow-hidden rounded-xl">
                <Image
                  src={entry.preview}
                  alt="cover"
                  width={400}
                  className="object-cover rounded-none h-[200px] w-full hover:scale-110 cursor-pointer"
                  onClick={handleOpenImage}
                />
              </div>
            </CardBody>
          ) : entry.image ? (
            <CardBody>
              <Button
                onClick={handleOpenImage}
                className="w-1/3"
                variant="flat"
              >
                Show Image
              </Button>
            </CardBody>
          ) : null}
        </Card>
      ) : (
        <Card>
          <CardHeader className="pb-0 pt-2.5 px-4 flex-col items-start">
            <div className="flex items-center gap-2 mb-2.5">
              <Avatar name={entry.user.charAt(0)} />
              <div className="flex flex-col">
                <p className="text-medium font-bold">{entry.user}</p>
                <Tooltip
                  classNames={{
                    base: "dark",
                    content: "dark text-white",
                  }}
                  showArrow={true}
                  content={formatDateTime(entry.time)}
                  delay={750}
                >
                  <small className="text-default-500">
                    {calcTime(entry.time)}
                  </small>
                </Tooltip>
              </div>
            </div>
            <h4 className="font-semibold">Drank {entry.tea.name}</h4>
          </CardHeader>
          {entry.preview ? (
            <CardBody className="overflow-visible py-2">
              <div className="h-[200px] overflow-hidden rounded-xl">
                <Image
                  src={entry.preview}
                  alt="cover"
                  width={400}
                  className="object-cover rounded-none h-[200px] w-full hover:scale-110 cursor-pointer"
                  onClick={handleOpenImage}
                />
              </div>
            </CardBody>
          ) : entry.image ? (
            <CardBody>
              <Button
                onClick={handleOpenImage}
                className="w-1/3"
                variant="flat"
              >
                Show Image
              </Button>
            </CardBody>
          ) : null}
        </Card>
      )}
      <Modal
        isOpen={showImage}
        onOpenChange={() => {
          setShowImage(false);
          setImage(null);
        }}
        className="dark text-white max-w-[90dvw] lg:max-w-fit absolute lg:scale-110"
      >
        <ModalContent>
          <ModalHeader>Image by {entry.user}</ModalHeader>
          <ModalBody className="pl-3 pr-3 pb-3">
            <Image src={image} alt="cover" width={400} height={400} />
          </ModalBody>
        </ModalContent>
      </Modal>
    </>
  );
};

export default FeedEntry;
