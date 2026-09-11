import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useMemo } from "react";
import { useAsyncCall } from "~/lib/api-call";
import { ProductService } from "~/services/product-service";
import type { Product, ProductFilter } from "~/types/product";

export function useCreateProduct() {
  const queryClient = useQueryClient();
  return useMutation<Product, Error, Partial<Product>>({
    mutationFn: (data) => ProductService.create(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["products"] });
    },
  });
}

export const useUpdateProduct = () => {
  const queryClient = useQueryClient();

  return useMutation<Product, Error, { id: number } & Partial<Product>>({
    mutationFn: ({ id, ...data }) => ProductService.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["products"] });
    },
  });
};

export function useGetProducts(filter?: Partial<ProductFilter>) {
  return useAsyncCall(() => ProductService.getAll(filter));
}

export function useGetProduct(id?: number) {
  const factory = useMemo(() => {
    if (!id || isNaN(id)) return undefined; // skip API call if invalid
    return () => ProductService.get(id);
  }, [id]);

  return useAsyncCall(factory);
}