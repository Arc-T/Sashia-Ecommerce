import ApiClient from "~/lib/api-client";
import { api } from "~/types/endpoints";
import type { PaginatedResponse } from "~/types/page";
import type { ServiceGroup, ServiceGroupFilter } from "~/types/service-groups";

export class ServiceGroupService {
  static async getAll(params?: Partial<ServiceGroupFilter>) {
    return ApiClient.for<PaginatedResponse<ServiceGroup>>(
      api.serviceGroups.list(params)
    ).get();
  }

  static async getById(id: number) {
    return ApiClient.for<ServiceGroup>(api.serviceGroups.details(id)).get();
  }

  static async create(data: Partial<ServiceGroup>) {
    return ApiClient.for<ServiceGroup>(api.serviceGroups.create)
      .onSuccess("گروه سرویس با موفقیت ایجاد شد")
      .onError("ایجاد گروه سرویس با خطا مواجه شد")
      .post(data);
  }

  static async update(id: number, data: Partial<ServiceGroup>) {
    return ApiClient.for<ServiceGroup>(api.serviceGroups.details(id))
      .onSuccess("گروه سرویس با موفقیت به‌روزرسانی شد")
      .onError("خطا در به‌روزرسانی گروه سرویس")
      .put(data);
  }

  static async delete(id: number) {
    return ApiClient.for<void>(api.serviceGroups.details(id))
      .onSuccess("گروه سرویس حذف شد")
      .onError("خطا در حذف گروه سرویس")
      .delete();
  }
}
