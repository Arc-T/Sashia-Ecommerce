import ApiClient from "~/lib/api-client";
import { api } from "~/types/endpoints";
import type { ProductMedia } from "~/types/product";

export class ProductMediaService {
  static async getAllDrafts(productId?: number) {
    return ApiClient.for<ProductMedia[]>(api.productMedia.draft(productId)).get();
  }

  static async getProductMedia(productId: number) {
    return ApiClient
      .for<ProductMedia[]>(api.productMedia.showProductMedia(productId))
      .get();
  }

  static async delete(id: number) {
    return ApiClient.for(api.productMedia.delete(id))
      .onSuccess("عکس با موفقیت حذف شد.")
      .onError("در حذف تصویر مشکلی رخ داده است!")
      .delete();
  }

  static async deleteDraft(mediaName: string, productId?: number) {
    return ApiClient.for(api.productMedia.deleteDraft(mediaName, productId))
      .onSuccess("عکس با موفقیت حذف شد.")
      .onError("در حذف تصویر مشکلی رخ داده است!")
      .delete();
  }

  static async upload(
    files: FormData,
    onProgress: (percentage: number) => void,
    productId?: number
  ) {
    onProgress(0);
    try {
      const result = await ApiClient.for<ProductMedia[]>(
        api.productMedia.draft(productId)
      )
        .setConfig({
          headers: {
            "Content-Type": "multipart/form-data",
          },
          timeout: 0,
          onUploadProgress(progressEvent) {
            const total = progressEvent.total ?? 0;
            if (!total) return;
            const percentage = Math.round((progressEvent.loaded * 100) / total);
            onProgress(percentage);
          },
        })
        .onSuccess("عکس ها با موفقیت آپلود شده اند.")
        .onError("در آپلود تصاویر مشکلی رخ داده است!")
        .post(files);

      onProgress(100);
      return result;
    } catch (e) {
      // you might want to reset or show error in caller
      throw e;
    }
  }
}
