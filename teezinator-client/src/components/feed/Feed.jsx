import { useState, useEffect, useCallback } from "react";
import FeedEntry from "./FeedEntry";
import axiosAuth from "../common/axiosAuth";

const Feed = () => {
  const [entries, setEntries] = useState([]);
  const [page, setPage] = useState(0);
  const [isLoading, setIsLoading] = useState(false);

  const fetchEntries = useCallback(async () => {
    if (isLoading) return;

    setIsLoading(true);
    document.getElementById("loading-progress").classList.remove("hidden");
    console.log(page);
    try {
      await axiosAuth.get("/stats/feed?page=" + page).then((response) => {
        setEntries((prevEntries) => [...prevEntries, ...response.data]);
        document.getElementById("loading-progress").classList.add("hidden");
      });
    } catch (error) {
      console.error(error);
    }
    setPage((prevPage) => prevPage + 1);
    setIsLoading(false);
  }, [page, isLoading]);

  useEffect(() => {
    fetchEntries();
  }, []);

  useEffect(() => {
    const handleScroll = () => {
      const { scrollTop, clientHeight, scrollHeight } =
        document.documentElement;
      if (scrollTop + clientHeight >= scrollHeight - 150) {
        fetchEntries();
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, [fetchEntries]);

  return (
      <div className="flex justify-center">
        <div className="flex flex-col justify-center w-3/4 lg:w-1/5 gap-5 pt-2 pb-6">
          <div className="font-semibold text-2xl text-center">
            Latest Entries
          </div>

          {entries.map((entry) => (
            <FeedEntry key={entry.id} entry={entry} />
          ))}
        </div>
      </div>
  );
};

export default Feed;
