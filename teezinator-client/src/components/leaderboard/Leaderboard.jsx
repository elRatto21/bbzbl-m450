import { useState, useEffect } from "react";
import axiosAuth from "../common/axiosAuth";
import { Bar } from "react-chartjs-2";
import {
  CategoryScale,
  LinearScale,
  Chart,
  BarElement,
  BarController,
} from "chart.js";
import ChartDataLabels from "chartjs-plugin-datalabels";

Chart.register(CategoryScale);
Chart.register(LinearScale);
Chart.register(BarElement);
Chart.register(BarController);

const Leaderboard = () => {
  const [chartData, setChartData] = useState({});

  const options = {
    indexAxis: "y",
    scales: {
      x: {
        beginAtZero: true,
        ticks: {
          color: "white",
          fontSize: 14,
        },
      },
      y: {
        ticks: {
          color: "white",
        },
        type: "category",
        labels: chartData.labels,
        beginAtZero: true,
      },
    },
    plugins: {
      legend: {
        labels: {
          fontColor: "white",
          fontSize: 14,
        },
      },
      datalabels: {
        color: "white",
        font: {
          size: 17,
        },
        anchor: "end",
        align: "center",
        formatter: function (value) {
          return "    " + value;
        },
      },
    },
    layout: {
      padding: {
        left: 20,
        right: 50,
        top: 0,
        bottom: 0,
      },
    },
  };

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    document.getElementById("loading-progress").classList.remove("hidden");
    await axiosAuth.get("/stats/getLifetimeLeaderboard").then((response) => {
      const labels = Object.keys(response.data);
      const values = Object.values(response.data);

      const chartData = {
        labels: labels,
        datasets: [
          {
            label: "Values",
            backgroundColor: "rgba(75,192,192,1)",
            borderColor: "rgba(0,0,0,1)",
            borderWidth: 1,
            data: values,
          },
        ],
      };
      setChartData(chartData);
      document.getElementById("loading-progress").classList.add("hidden");
    });
  };

  return (
    <>
      <div className="font-semibold text-2xl text-center pt-2">Leaderboard</div>
      <div className="lg:hidden w-full h-fit">
        {chartData && chartData.labels && chartData.labels.length > 0 && (
          <Bar
            data={chartData}
            options={options}
            plugins={[ChartDataLabels]}
          />
        )}
      </div>
      <div className="w-full flex justify-center">
        <div className="hidden lg:block w-1/2">
          {chartData && chartData.labels && chartData.labels.length > 0 && (
            <Bar
              data={chartData}
              options={options}
              plugins={[ChartDataLabels]}
            />
          )}
        </div>
      </div>
    </>
  );
};

export default Leaderboard;
