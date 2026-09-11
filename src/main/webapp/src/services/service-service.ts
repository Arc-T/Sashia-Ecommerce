import ApiClient from "~/lib/api-client";
import {api} from "~/types/endpoints";
import type { PaginatedResponse } from "~/types/page";
import type {Service, ServiceFilter} from "~/types/services";

export class ServiceService {

    static async getAll(params?: Partial<ServiceFilter>) {
        return ApiClient
            .for<PaginatedResponse<Service>>(api.services.list(params))
            .get();
    }

    static async getById(id: number) {
        return ApiClient
            .for<Service>(api.services.details(id))
            .get();
    }

    static async create(data: Partial<Service>) {
        return ApiClient
            .for<Service>(api.services.create)
            .onSuccess("سرویس با موفقیت ایجاد شد")
            .onError("ایجاد سرویس با خطا مواجه شد")
            .post(data);
    }

    static async update(id: number, data: Partial<Service>) {
        return ApiClient
            .for<Service>(api.services.update(id))
            .onSuccess("سرویس با موفقیت به‌روزرسانی شد")
            .onError("خطا در به‌روزرسانی سرویس")
            .put(data);
    }

    static async delete(id: number) {
        return ApiClient
            .for<void>(api.services.details(id))
            .onSuccess("سرویس حذف شد")
            .onError("خطا در حذف سرویس")
            .delete();
    }

}
