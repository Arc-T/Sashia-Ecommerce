import { Skeleton } from "@/components/ui/skeleton";
import { FieldSkeleton } from "../components/custom/elements/field";

export function CustomIsLoadingPage() {
  return (
    <>
      <div className="flex items-center gap-4 pb-3">
        <Skeleton className="h-14 w-14 rounded-xl" />
        <div className="space-y-2">
          <Skeleton className="h-6 w-48" />
          <Skeleton className="h-4 w-64" />
        </div>
      </div>

      <div className="rounded-2xl border border-border bg-card p-4 shadow-sm sm:p-6">
        <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
          {[...Array(6)].map((_, i) => (
            <FieldSkeleton key={i} />
          ))}
        </div>
      </div>
    </>
  );
}
