import type { PaginatedRequest } from "./page";

export interface ServiceGroup {
  id: number;
  name: string;
  description: string;
  createdAt: string;
  updatedAt: string;
}

export interface ServiceGroupFilter extends PaginatedRequest {
  name: string;
}
