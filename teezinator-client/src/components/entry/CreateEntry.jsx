import {
  Select,
  SelectItem,
  DatePicker,
  Button,
  Input,
  Switch,
  cn,
} from "@nextui-org/react";
import { teas, water, coffee, chocolate, types } from "./data";
import { now, getLocalTimeZone } from "@internationalized/date";
import { useRef, useState } from "react";
import imageCompression from "browser-image-compression";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import moment from "moment";
import axiosAuth from "../common/axiosAuth";

const CreateEntry = () => {
  const [image, setImage] = useState(null);
  const [imagePreview, setImagePreview] = useState(null);
  const [fileName, setFileName] = useState("");

  const [sugar, setSugar] = useState(false);

  const [type, setType] = useState("");

  const [entryType, setEntryType] = useState("tea");

  const [data, setData] = useState(teas);

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
    if (!type || !timeOfConsumption) {
      toast.error("Please fill all the fields");
      return;
    }
    try {
      const data = JSON.stringify({
        teaId: type.target.value,
        sugar: sugar,
        timeOfConsumption: moment(timeOfConsumption)
          .subtract(1, "months")
          .format(),
        image: image,
        imagePreview: imagePreview,
        type: "REGULAR"
      });
      await axiosAuth.post("/tea", data).then(() => {
        toast.success("Successfully added tea!");
        navigate("/");
      });
      setType("");
      setImage(null);
      setImagePreview(null);
      setFileName("");
      setEntryType("tea");
    } catch (error) {
      console.log(error);
      toast.error("Failed to add tea");
    }
  };

  const handleTypeChange = (value) => {
    setEntryType(value.target.value);
    if (value.target.value === "water" || value.target.value === "chocolate") {
      setSugar(false);
    }
    setData(
      value.target.value === "tea"
        ? teas
        : value.target.value === "water"
        ? water
        : value.target.value === "coffee"
        ? coffee
        : chocolate
    );
  };

  return (
    <div className="flex justify-center">
      <div className="flex flex-col justify-center w-3/4 lg:w-1/4 gap-5 pt-4">
        <div className="font-bold text-3xl">Add Entry</div>
        <Select
          label="Select entry type"
          classNames={{
            base: "dark",
            popoverContent: "dark text-white",
          }}
          value={entryType}
          onChange={handleTypeChange}
          defaultSelectedKeys={["tea"]}
        >
          {types.map((type) => (
            <SelectItem value={type.id} key={type.id}>
              {type.name}
            </SelectItem>
          ))}
        </Select>
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
        {entryType === "tea" || entryType === "coffee" ? (
          <Switch
            classNames={{
              base: cn(
                "inline-flex flex-row-reverse max-w-full bg-[#27272a] hover:bg-content2 items-center",
                "justify-between cursor-pointer rounded-[12px] gap-2 pt-3.5 pb-3.5 pl-3 border-2 border-transparent"
              ),
              wrapper: "p-0 h-4 overflow-visible",
              thumb: cn(
                "w-6 h-6 border-2 shadow-lg",
                "group-data-[hover=true]:border-primary",
                "group-data-[selected=true]:ml-6",
                "group-data-[pressed=true]:w-7",
                "group-data-[selected]:group-data-[pressed]:ml-4"
              ),
            }}
            value={sugar}
            onValueChange={setSugar}
          >
            <div className="flex flex-col gap-1">
              <p className="text-medium">Sugar</p>
            </div>
          </Switch>
        ) : null}
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

export default CreateEntry;
