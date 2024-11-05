import {
  Select,
  SelectItem,
  DatePicker,
  Button,
  Input,
  Switch,
  cn,
} from "@nextui-org/react";
import { now, getLocalTimeZone } from "@internationalized/date";
import { useRef, useState } from "react";
import imageCompression from "browser-image-compression";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import moment from "moment";
import axiosAuth from "../common/axiosAuth";
import {
  types,
  drinks,
  curry,
  grill,
  iceCream,
  pinsa,
  pita,
  rice,
  wraps,
} from "./vibesData";

const CreateVibesEntry = () => {
  const [image, setImage] = useState(null);
  const [imagePreview, setImagePreview] = useState(null);
  const [fileName, setFileName] = useState("");

  const [type, setType] = useState("");

  const [entryType, setEntryType] = useState("drinks");

  const [data, setData] = useState(drinks);

  const [timeOfConsumption, setTimeOfConsumption] = useState(
    now(getLocalTimeZone())
  );

  const imageUploadRef = useRef(null);

  const navigate = useNavigate();

  const handleImageChange = async (event) => {
    const file = event.target.files[0];
    if (file) {
      const options = {
        maxSizeMB: 0.02,
        maxWidthOrHeight: 400,
        useWebWorker: true,
        maxIteration: 30,
      };
      try {
        const compressedFile = await imageCompression(file, options);
        const reader = new FileReader();
        reader.onload = () => {
          const base64Image = reader.result;
          setImagePreview(base64Image);
        };
        reader.readAsDataURL(compressedFile);
      } catch (error) {
        console.error(error);
      }
    } else {
      setImagePreview(null);
      event.target.value = "";
    }
    if (file) {
      const options = {
        maxSizeMB: 0.6,
        maxWidthOrHeight: 1920,
        useWebWorker: true,
        maxIteration: 20,
      };
      try {
        const compressedFile = await imageCompression(file, options);
        const reader = new FileReader();
        reader.onload = () => {
          const base64Image = reader.result;
          setImage(base64Image);
          setFileName(compressedFile.name);
        };
        reader.readAsDataURL(compressedFile);
      } catch (error) {
        console.error(error);
      }
    } else {
      setImage(null);
      setFileName("");
      event.target.value = "";
    }
  };

  const handleSubmit = async () => {
    if (
      ((!type || !timeOfConsumption) && entryType !== "image") ||
      (entryType === "image" && (!image || !timeOfConsumption))
    ) {
      toast.error("Please fill all the fields");
      return;
    }
    try {
      let data;
      if (entryType === "image") {
        data = JSON.stringify({
          teaId: "66743e9b221230b360ceed02",
          sugar: false,
          timeOfConsumption: moment(timeOfConsumption)
            .subtract(1, "months")
            .format(),
          image: image,
          imagePreview: imagePreview,
          type: "POST"
        });
      } else {
        data = JSON.stringify({
          teaId: type.target.value,
          sugar: false,
          timeOfConsumption: moment(timeOfConsumption)
            .subtract(1, "months")
            .format(),
          image: image,
          imagePreview: imagePreview,
          type: "SUMMERVIBES"
        });
      }
      await axiosAuth.post("/tea", data).then(() => {
        toast.success("Successfully added entry!");
        navigate("/");
      });
      setType("");
      setImage(null);
      setImagePreview(null);
      setFileName("");
      setEntryType("tea");
    } catch (error) {
      console.log(error);
      toast.error("Failed to add entry");
    }
  };

  const handleTypeChange = (value) => {
    setEntryType(value.target.value);
    if (value.target.value === "image") {
      setType("image");
      return;
    }
    setData(
      value.target.value === "drinks"
        ? drinks
        : value.target.value === "grill"
        ? grill
        : value.target.value === "greekPita"
        ? pita
        : value.target.value === "friedRice"
        ? rice
        : value.target.value === "summerWraps"
        ? wraps
        : value.target.value === "jerkCurry"
        ? curry
        : value.target.value === "iceCream"
        ? iceCream
        : pinsa
    );
  };

  return (
    <div className="flex justify-center">
      <div className="flex flex-col justify-center w-3/4 lg:w-1/4 gap-5 pt-4">
        <div className="font-bold text-3xl">Add Summer Vibes Entry</div>
        <Select
          label="Select entry type"
          classNames={{
            base: "dark",
            popoverContent: "dark text-white",
          }}
          value={entryType}
          onChange={handleTypeChange}
          defaultSelectedKeys={["drinks"]}
        >
          {types.map((type) => (
            <SelectItem value={type.id} key={type.id}>
              {type.name}
            </SelectItem>
          ))}
        </Select>
        {entryType !== "image" && (
          <Select
            label={"Select " + entryType + " type"}
            classNames={{
              base: "dark",
              popoverContent: "dark text-white",
            }}
            value={type}
            onChange={setType}
          >
            {data.map((entry) => (
              <SelectItem value={entry.name} key={entry.id}>
                {entry.name}
              </SelectItem>
            ))}
          </Select>
        )}
        <DatePicker
          label="Time of consumption"
          hideTimeZone
          showMonthAndYearPickers
          hourCycle="24"
          classNames={{
            base: "dark",
            popoverContent: "dark text-white",
          }}
          value={timeOfConsumption}
          onChange={setTimeOfConsumption}
        />
        <input
          accept="image/*"
          type="file"
          className="hidden"
          onChange={handleImageChange}
          ref={imageUploadRef}
        />
        {image ? (
          <Input
            isClearable
            isReadOnly
            label="Uploaded image"
            defaultValue={fileName}
            onClear={() => {
              setFileName("");
              setImage(null);
            }}
          />
        ) : (
          <Button
            className="w-2/5"
            onClick={() => imageUploadRef.current.click()}
          >
            Upload image
          </Button>
        )}
        <Button color="primary" className="mt-7" onClick={handleSubmit}>
          Add
        </Button>
      </div>
    </div>
  );
};

export default CreateVibesEntry;
