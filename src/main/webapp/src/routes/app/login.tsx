import { Eye, EyeOff, User } from "lucide-react";
import { useState } from "react";

import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";

import { Toaster } from "@/components/ui/sonner";
import { useIsMobile } from "@/hooks/use-mobile";
import { cn } from "@/lib/utils";
import { useTranslation } from "react-i18next";

import { useMutateAuthenticate } from "@/hooks/use-user";
import {
  useAuthenticateForm,
  type AuthenticateUserFormValues,
} from "@/types/user";

import { RHFSubmitButton } from "@/components/custom/elements/form/button";
import { RHFInput } from "@/components/custom/elements/form/input";
import { AUTH_TOKEN_KEY } from "@/types/auth";
import { useNavigate } from "react-router";

export function LoginPage() {
  const [showPassword, setShowPassword] = useState(false);

  const navigate = useNavigate();
  const isMobile = useIsMobile();
  const { i18n } = useTranslation();

  const { mutate: authenticate, isPending } = useMutateAuthenticate();

  const {
    control,
    handleSubmit,
    formState: { isValid },
  } = useAuthenticateForm();

  function onSubmit(data: AuthenticateUserFormValues) {
    authenticate(data, {
      onSuccess: (response) => {
        localStorage.setItem(AUTH_TOKEN_KEY, response.token);
        navigate("/dashboard", { replace: true });
      },

      onError: (err) => {
        console.error("Login failed:", err);
      },
    });
  }

  return (
    <div
      className="min-h-screen flex items-center justify-center bg-background"
      dir="rtl"
    >
      <Toaster
        dir={i18n.dir()}
        position="top-center"
        expand
        richColors
        duration={4000}
        className={cn("sm:max-w-105", isMobile && "bottom-4 top-auto")}
      />

      <div className="w-full max-w-md space-y-8">
        <Card className="w-full">
          <CardHeader className="text-center space-y-2">
            <CardTitle className="text-2xl font-bold tracking-tight">
              ورود به حساب کاربری
            </CardTitle>
            <CardDescription>
              لطفاً شماره موبایل و رمز عبور خود را وارد کنید
            </CardDescription>
          </CardHeader>

          <CardContent>
            <form className="space-y-4" onSubmit={handleSubmit(onSubmit)}>
              <RHFInput
                control={control}
                name="username"
                type="tel"
                placeholder="form_enter_username"
                label="username_label"
                icon={<User />}
              />

              <RHFInput
                control={control}
                name="password"
                type={showPassword ? "text" : "password"}
                placeholder="form_enter_password"
                label="password"
                icon={
                  showPassword ? (
                    <EyeOff
                      className="h-4 w-4 cursor-pointer"
                      onClick={() => setShowPassword(false)}
                    />
                  ) : (
                    <Eye
                      className="h-4 w-4 cursor-pointer"
                      onClick={() => setShowPassword(true)}
                    />
                  )
                }
              />

              <RHFSubmitButton
                isSubmitting={isPending}
                defaultText="login"
                loadingText="loading"
                disabled={!isValid}
              />
            </form>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
