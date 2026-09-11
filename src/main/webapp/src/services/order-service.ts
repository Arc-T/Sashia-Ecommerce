import ApiClient from "~/lib/api-client";
import { api } from "~/types/endpoints";
import type { Order, OrderFilter } from "~/types/order";
import type { PaginatedResponse } from "~/types/page";

export class OrderService {

    static async getAll(filter?: Partial<OrderFilter>) {
        return ApiClient
            .for<PaginatedResponse<Order>>(api.orders.list(filter))
            .get();
    }

}