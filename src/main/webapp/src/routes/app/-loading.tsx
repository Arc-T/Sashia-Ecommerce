import { Spinner } from "@/components/ui/spinner";
import { t } from "i18next";

export const LoadingFallback = ({ message }: { message: string }) => (
  <div className="flex min-h-[40vh] items-center justify-center text-muted-foreground">
    <Spinner className="mr-2" />
    {t(message)}
  </div>
);
