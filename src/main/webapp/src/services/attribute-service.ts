import ApiClient from "@/lib/api-client";
import type {
  Attribute,
  AttributeFilter,
  CreateAttributeDTO,
} from "@/types/attribute";
import { api } from "@/types/endpoints";
import type { PaginatedResponse } from "@/types/page";

export class AttributeService {
  static async getAll(params?: Partial<AttributeFilter>) {
    return ApiClient.for<PaginatedResponse<Attribute>>(
      api.attributes.list(params),
    ).get();
  }

  static async getAllTypes() {
    return ApiClient.for<string[]>(api.attributes.types).get();
  }

  static async getById(id: number) {
    return ApiClient.for<Attribute>(api.attributes.details(id)).get();
  }

  static async create(body: CreateAttributeDTO) {
    return ApiClient.for<Attribute>(api.attributes.create)
      .onSuccess("ویژگی با موفقیت ایجاد شد")
      .onError("ایجاد ویژگی با خطا مواجه شد")
      .post(body);
  }

  static async update(id: number, data: CreateAttributeDTO) {
    return ApiClient.for<Attribute>(api.attributes.update(id))
      .onSuccess("ویژگی با موفقیت به‌روزرسانی شد")
      .onError("خطا در به‌روزرسانی ویژگی")
      .put(data);
  }

  static async delete(id: number) {
    return ApiClient.for<void>(api.attributes.delete(id))
      .onSuccess("ویژگی حذف شد")
      .onError("خطا در حذف ویژگی")
      .delete();
  }
}
