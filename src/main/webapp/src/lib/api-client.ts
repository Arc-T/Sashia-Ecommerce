import i18n from "@/lang/i18n";
import { AUTH_TOKEN_KEY } from "@/types/auth";
import axios, {
  type AxiosInstance,
  type AxiosRequestConfig,
} from "axios";
import { toast } from "sonner";

const API_BASE_URL = import.meta.env.VITE_API_URL;
const DEFAULT_TIMEOUT = 5000;

const axiosInstance: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: DEFAULT_TIMEOUT,
  withCredentials: true,
  headers: { "Accept-Language": "fa-IR" },
});

// axiosInstance.interceptors.response.use(
//   (response) => response,
//   (error) => {
//     if ([401, 403].includes(error.response?.status)) {
//       localStorage.removeItem(AUTH_TOKEN_KEY)
//       window.location.href = "/login";
//     }
//     return Promise.reject(error);
//   },
// );

axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem(AUTH_TOKEN_KEY);

  if (token) {
    config.headers = config.headers ?? {};
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

const toastError = (message: string, suppress = false) => {
  if (!suppress) toast.error(message);
};
const toastSuccess = (message: string, suppress = false) => {
  if (!suppress) toast.success(message);
};

export class ApiClient<T = unknown> {
  private endpoint: string;
  private config: AxiosRequestConfig = {};
  private successMessage?: string;
  private errorMessage?: string;
  private suppressToasts = false;

  constructor(endpoint: string = "") {
    this.endpoint = endpoint;
  }

  setConfig(customConfig: AxiosRequestConfig = {}) {
    this.config = { ...this.config, ...customConfig };
    return this;
  }

  onSuccess(message: string) {
    this.successMessage = message;
    return this;
  }

  onError(message: string) {
    this.errorMessage = message;
    return this;
  }

  suppress(flag = true) {
    this.suppressToasts = flag;
    return this;
  }

  private async request(
    method: "GET" | "POST" | "PUT" | "PATCH" | "DELETE",
    payload?: unknown,
  ): Promise<T> {
    try {
      const response = await axiosInstance({
        method: method.toLowerCase() as string,
        url: this.endpoint,
        data: payload,
        ...this.config,
      });

      if (this.successMessage) {
        toastSuccess(i18n.t(this.successMessage), this.suppressToasts);
      }

      return response.data;
    } catch (error: any) {
      const message =
        this.errorMessage ||
        error.response?.data?.message ||
        error.response?.data?.error ||
        (!error.response && "اتصال به سرور برقرار نشد") ||
        (error.code === "ECONNABORTED"
          ? "درخواست شما به دلیل زمان طولانی لغو شد"
          : "خطای نامشخص رخ داده است");

      toastError(i18n.t(message), this.suppressToasts);

      throw error;
    }
  }

  get() {
    return this.request("GET");
  }

  post(payload?: unknown) {
    return this.request("POST", payload);
  }

  put(payload?: unknown) {
    return this.request("PUT", payload);
  }

  patch(payload?: unknown) {
    return this.request("PATCH", payload);
  }

  delete(payload?: unknown) {
    return this.request("DELETE", payload);
  }

  static for<T = unknown>(endpoint: string): ApiClient<T> {
    return new ApiClient<T>(endpoint);
  }
}

export default ApiClient;
