import ApiClient from "~/lib/api-client";
import { api } from "~/types/endpoints";
import type { PaginatedResponse } from "~/types/page";
import type { Product, ProductFilter } from "~/types/product";

export class ProductService {

  static get(id: number) {
    return ApiClient
      .for<Product>(api.products.get(id))
      .get();
  }

  static getAll(params?: Partial<ProductFilter>) {
    return ApiClient
      .for<PaginatedResponse<Product>>(api.products.list(params))
      .get();
  }

  static getById(id: number) {
    return ApiClient.for<Product>(api.products.details(id)).get();
  }

  static create(data: Partial<Product>) {
    return ApiClient.for<Product>(api.products.create)
      .setConfig({ headers: { "Content-Type": "multipart/form-data" } })
      .onSuccess("محصول با موفقیت ایجاد شد")
      .onError("ایجاد محصول با خطا مواجه شد")
      .post(data);
  }

  static update(id: number, data: Partial<Product>) {
    return ApiClient.for<Product>(api.products.update(id))
      .onSuccess("محصول با موفقیت به‌روزرسانی شد")
      .onError("خطا در به‌روزرسانی محصول")
      .put(data);
  }

  static delete(id: number) {
    return ApiClient.for<void>(api.products.details(id))
      .onSuccess("محصول حذف شد")
      .onError("خطا در حذف محصول")
      .delete();
  }
  
}
