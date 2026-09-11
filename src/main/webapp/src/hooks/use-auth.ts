import { UserService } from "@/services/user-service";
import { useQuery } from "@tanstack/react-query";

export function useAuthSession() {
  return useQuery({
    queryKey: ["auth"],
    queryFn: UserService.validateUserFromSession,
    retry: false,
    staleTime: 1000 * 60 * 5,
    gcTime: 1000 * 60 * 30, // Keep in cache for 30 minutes
    refetchOnMount: true,
    refetchOnWindowFocus: true,
    refetchOnReconnect: true,
  });
}
