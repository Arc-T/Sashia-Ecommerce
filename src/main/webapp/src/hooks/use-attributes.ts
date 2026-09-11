import { AttributeService } from "@/services/attribute-service";
import type { AttributeFilter, CreateAttributeDTO } from "@/types/attribute";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

export const attributeKeys = {
  one: (id: number) => ["attribute", id] as const,
  all: ["attributes"] as const,
  types: ["attribute-types"] as const,
  list: (filter?: Partial<AttributeFilter>) =>
    [...attributeKeys.all, "list", filter] as const,
};

export const useInvalidateAttributes = () => {
  const queryClient = useQueryClient();

  return () =>
    queryClient.invalidateQueries({
      queryKey: attributeKeys.all,
    });
};

export const useCreateAttribute = () => {
  const invalidate = useInvalidateAttributes();

  return useMutation({
    mutationFn: AttributeService.create,
    onSuccess: invalidate,
  });
};

export const useGetAttributes = (filter?: Partial<AttributeFilter>) => {
  return useQuery({
    queryKey: attributeKeys.list(filter),
    queryFn: () => AttributeService.getAll(filter),
  });
};

export const useGetAttributeTypes = () => {
  return useQuery({
    queryKey: attributeKeys.types,
    queryFn: () => AttributeService.getAllTypes(),
  });
};

export const useGetAttribute = (id: number) => {
  return useQuery({
    queryKey: attributeKeys.one(id),
    queryFn: () => AttributeService.getById(id),
    enabled: !!id,
    staleTime: 0,
  });
};

export const useUpdateAttribute = () => {
  const invalidate = useInvalidateAttributes();

  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: CreateAttributeDTO }) =>
      AttributeService.update(id, data),
    onSuccess: invalidate,
  });
};
