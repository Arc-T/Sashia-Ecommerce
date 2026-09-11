import ApiClient from "@/lib/api-client";
import type { Category, CategoryFilter } from "@/types/category";
import { api } from "@/types/endpoints";
import type { PaginatedResponse } from "@/types/page";

export class CategoryService {
  static getAll(params?: Partial<CategoryFilter>) {
    return ApiClient.for<PaginatedResponse<Category>>(
      api.categories.list(params),
    ).get();
  }

  static getById(id: number) {
    return ApiClient.for<Category>(api.categories.details(id)).get();
  }

  static create(data: Partial<Category>) {
    return ApiClient.for<Category>(api.categories.create)
      .onSuccess("دسته بندی با موفقیت ایجاد شد")
      .onError("ایجاد دسته بندی با خطا مواجه شد")
      .post(data);
  }

  static update(id: number, data: Partial<Category>) {
    return ApiClient.for<Category>(api.categories.details(id))
      .onSuccess("دسته بندی با موفقیت به‌روزرسانی شد")
      .onError("خطا در به‌روزرسانی دسته بندی")
      .put(data);
  }

  static delete(id: number) {
    return ApiClient.for<void>(api.categories.details(id))
      .onSuccess("دسته بندی حذف شد")
      .onError("خطا در حذف دسته بندی")
      .delete();
  }
  
}
