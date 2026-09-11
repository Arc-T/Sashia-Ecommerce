import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function mapToSelectOptions<T extends { id: number; name: string }>(
  items?: T[],
) {
  return (
    items?.map((item) => ({
      value: String(item.id),
      label: item.name,
    })) ?? []
  );
}

export function toQueryString(params?: Record<string, string>): string {
  if (!params) return "";
  const query = Object.entries(params)
    .filter(
      ([_, value]) => value !== undefined && value !== null && value !== "",
    )
    .map(
      ([key, value]) =>
        `${encodeURIComponent(key)}=${encodeURIComponent(value)}`,
    )
    .join("&");
  return query ? `?${query}` : "";
}

export function formatPrice(value: string | number) {
  return typeof value === "string" ? Number(value.replace(/\D/g, "")) : value;
}
