import { CategoryService } from "@/services/category-service";
import type { Category, CategoryFilter } from "@/types/category";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

export const useGetCategories = (filter?: Partial<CategoryFilter>) => {
  return useQuery({
    queryKey: ["categories", filter],
    queryFn: () => CategoryService.getAll(filter),
  });
};

export function useCreateCategory() {
  const queryClient = useQueryClient();

  return useMutation<Category, Error, Partial<Category>>({
    mutationFn: (data) => CategoryService.create(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["categories"] });
    },
  });
}

export function useUpdateCategory() {
  const queryClient = useQueryClient();

  return useMutation<Category, Error, { id: number } & Partial<Category>>({
    mutationFn: ({ id, ...data }) => CategoryService.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["categories"] });
    },
  });
}
