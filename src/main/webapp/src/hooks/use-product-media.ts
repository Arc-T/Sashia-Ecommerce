import { useMutation } from "@tanstack/react-query";
import { useAsyncCall } from "~/lib/api-call";
import { ProductMediaService } from "~/services/product-media-service";

export function useGetDraftMedia(productId?: number) {
  return useAsyncCall(() => ProductMediaService.getAllDrafts(productId));
}

export function useDeleteDraftMedia() {
  return useMutation<
    unknown,
    Error,
    { mediaName: string; productId?: number }
  >({
    mutationFn: ({ mediaName, productId }) =>
      ProductMediaService.deleteDraft(mediaName, productId),
  });
}

export function useDeleteMedia() {
  return useMutation<
    unknown,
    Error,
    { id: number; }
  >({
    mutationFn: ({ id }) =>
      ProductMediaService.delete(id),
  });
}

