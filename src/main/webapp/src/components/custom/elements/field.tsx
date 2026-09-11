import { Skeleton } from "@/components/ui/skeleton";
import { cn } from "@/lib/utils";
import { useTranslation } from "react-i18next";

interface Props {
  label: string;
  value: React.ReactNode;
  icon?: React.ReactNode;
  className?: string;
}

export function CustomField({ label, value, icon, className }: Props) {
  const { t } = useTranslation();
  return (
    <>
      <div className={cn("space-y-2", className)}>
        <label className="flex items-center gap-2 text-sm font-medium text-muted-foreground">
          {icon && <span className="h-4 w-4">{icon}</span>}
          {t(label)}
        </label>
        <div className="rounded-lg border border-border bg-muted/30 px-4 py-3 transition-colors hover:bg-muted/50">
          {value ?? "-"}
        </div>
      </div>
    </>
  );
}

export const FieldSkeleton = () => (
  <div className="space-y-2">
    <Skeleton className="h-4 w-20" />
    <Skeleton className="h-12 w-full" />
  </div>
);
