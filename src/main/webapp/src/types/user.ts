import z from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

export interface User {
  name: string;
  family_name: string;
}

export interface UserLogin {
  username: string;
  password: string;
}

export type AuthenticateUserFormValues = z.infer<typeof UserSchema>;

export const UserSchema = z.object({
  username: z.string().min(1),
  password: z.string().min(3),
  rememberMe: z.boolean().optional(),
});

export const useAuthenticateForm = () =>
  useForm<AuthenticateUserFormValues>({
    resolver: zodResolver(UserSchema),
    mode: "onSubmit",
    defaultValues: {
      username: "",
      password: "",
      rememberMe: false,
    },
  });
