import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useAsyncCall } from "~/lib/api-call";
import { ServiceService } from "~/services/service-service";
import type { Service, ServiceFilter } from "~/types/services";

export function useCreateService() {
  const queryClient = useQueryClient();

  return useMutation<Service, Error, Partial<Service>>({
    mutationFn: (data) => ServiceService.create(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["services"] });
    },
  });
}

export const useUpdateService = () => {
  return useMutation<Service, Error, { id: number } & Partial<Service>>({
    mutationFn: ({ id, ...data }) => ServiceService.update(id, data),
  });
};

export function useGetServices(filter?: Partial<ServiceFilter>) {
  return useAsyncCall(() => ServiceService.getAll(filter));
}