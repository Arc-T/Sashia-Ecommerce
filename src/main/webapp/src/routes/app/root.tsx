import { Outlet, Navigate } from "react-router";
import { LoadingFallback } from "./-loading";
import { useAuthSession } from "@/hooks/use-auth";

export function RootLayout() {
  const { data, isLoading, isError } = useAuthSession();

  if (isLoading) {
    return <LoadingFallback message="validating" />;
  }

  if (isError || !data) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
}
