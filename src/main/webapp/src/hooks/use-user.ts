import { UserService } from "@/services/user-service";
import type { User } from "@/types/user";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

export const useValidateUser = () => {
  const queryClient = useQueryClient();
  return useMutation<User>({
    mutationFn: UserService.validateUserFromSession,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["user"] });
    },
  });
};

export const useValidateSession = () => {
  return useQuery({
    queryKey: ["user"],
    queryFn: UserService.validateUserFromSession,
    staleTime: 5 * 60 * 1000,
    retry: false,
  });
};

export const useMutateAuthenticate = () => {
  return useMutation({
    mutationFn: UserService.authenticate,
  });
};

export const useLogout = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: UserService.logout,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["user"] });
    },
  });
};