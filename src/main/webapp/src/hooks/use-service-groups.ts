import { useAsyncCall } from "~/lib/api-call";
import { ServiceGroupService } from "~/services/service-group-service";
import type { ServiceGroupFilter } from "~/types/service-groups";

export function useGetServiceGroups(filter?: Partial<ServiceGroupFilter>) {
  return useAsyncCall(() => ServiceGroupService.getAll(filter));
}