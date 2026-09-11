import type { PaginatedRequest } from "./page";

export interface Order {
    id: number;
    userId: number;
    totalPrice: number;
    status: OrderStatus;
    description: string;
    createdAt: string;
    updatedAt: string;
}

export interface OrderFilter extends PaginatedRequest {
    id: number;
    name: string;
}

export enum OrderStatus {
    PENDING, PROCESSING, DELIVERED, SHIPPED, CANCELLED
}