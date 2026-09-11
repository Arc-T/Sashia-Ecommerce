import {
  flexRender,
  getCoreRowModel,
  getSortedRowModel,
  useReactTable,
  type ColumnDef,
  type SortingState,
} from "@tanstack/react-table";
import type { UseQueryResult } from "@tanstack/react-query";
import { ArrowDown, ArrowUp, ArrowUpDown, Package } from "lucide-react";
import { motion } from "motion/react";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import { toast } from "sonner";

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Skeleton } from "@/components/ui/skeleton";

import Pagination from "./pagination";
import ActionBar from "./search-bar";
import { DataTableFetchError } from "./fetch-error";

import { cn } from "@/lib/utils";
import type { PaginatedRequest, PaginatedResponse } from "@/types/page";

interface RowData {
  id: number;
}

interface Props<T extends RowData> {
  query: UseQueryResult<PaginatedResponse<T>>;
  columns: ColumnDef<T, string>[];
  searchComponent?: React.ReactNode;
  onRowClick?: (id: number) => void;
  onPaginationClick: (page: PaginatedRequest) => void;
  pageSize?: number;
}

export default function DataTable<T extends RowData>({
  query,
  columns,
  searchComponent,
  onRowClick,
  onPaginationClick,
  pageSize = 5,
}: Props<T>) {
  const { t } = useTranslation();

  const [sorting, setSorting] = useState<SortingState>([]);
  const [page, setPage] = useState<PaginatedRequest>({
    page: 0,
    size: pageSize,
  });

  const [isRefreshing, setIsRefreshing] = useState(false);

  const table = useReactTable({
    data: query.data?.content ?? [],
    columns,
    state: { sorting },
    onSortingChange: setSorting,
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
    manualPagination: true,
    pageCount: query.data?.page?.totalPages ?? 0,
  });

  const updatePagination = (next: Partial<PaginatedRequest>) => {
    const updated = {
      ...page,
      ...next,
    };

    setPage(updated);
    onPaginationClick(updated);
  };

  const handleRefresh = async () => {
    setIsRefreshing(true);

    try {
      await query.refetch();
      toast.success(t("data_refresh_success"));
    } catch {
      toast.error(t("data_refresh_fail"));
    } finally {
      setIsRefreshing(false);
    }
  };

  if (query.isError) {
    return <DataTableFetchError handleRefresh={query.refetch} />;
  }

  return (
    <div className="rounded-xl border bg-card">
      <ActionBar handleRefresh={handleRefresh} isRefreshing={isRefreshing}>
        {searchComponent}
      </ActionBar>

      <div className="mx-4 overflow-hidden rounded-xl bg-gray-50 px-4 py-2 dark:bg-background">
        <div className="overflow-x-auto">
          <Table>
            <TableHeader>
              {table.getHeaderGroups().map((group) => (
                <TableRow key={group.id}>
                  {group.headers.map((header) => {
                    const canSort = header.column.getCanSort();

                    return (
                      <TableHead
                        key={header.id}
                        onClick={header.column.getToggleSortingHandler()}
                        className={cn(
                          "text-xs uppercase tracking-wider text-muted-foreground",
                          canSort &&
                            "cursor-pointer select-none hover:text-foreground",
                        )}
                      >
                        <div className="flex items-center gap-1">
                          {flexRender(
                            header.column.columnDef.header,
                            header.getContext(),
                          )}

                          {canSort &&
                            (header.column.getIsSorted() === "asc" ? (
                              <ArrowUp className="h-3 w-3 text-indigo-500" />
                            ) : header.column.getIsSorted() === "desc" ? (
                              <ArrowDown className="h-3 w-3 text-indigo-500" />
                            ) : (
                              <ArrowUpDown className="h-3 w-3 opacity-40" />
                            ))}
                        </div>
                      </TableHead>
                    );
                  })}
                </TableRow>
              ))}
            </TableHeader>

            <TableBody>
              {query.isLoading ? (
                Array.from({ length: page.size }).map((_, i) => (
                  <TableRow key={i}>
                    {columns.map((_, j) => (
                      <TableCell key={j}>
                        <Skeleton className="h-4 w-full max-w-28" />
                      </TableCell>
                    ))}
                  </TableRow>
                ))
              ) : table.getRowModel().rows.length === 0 ? (
                <TableRow>
                  <TableCell
                    colSpan={columns.length}
                    className="h-48 text-center"
                  >
                    <div className="flex flex-col items-center gap-3 text-muted-foreground">
                      <Package className="h-10 w-10 opacity-40" />
                      <p className="text-sm">{t("no_data")}</p>
                    </div>
                  </TableCell>
                </TableRow>
              ) : (
                table.getRowModel().rows.map((row, index) => (
                  <motion.tr
                    key={row.id}
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    transition={{ delay: index * 0.02 }}
                    onClick={() => onRowClick?.(row.original.id)}
                    className={cn(
                      "border-b transition-colors last:border-0",
                      onRowClick &&
                        "cursor-pointer hover:bg-white/75 dark:hover:bg-muted/20",
                    )}
                  >
                    {row.getVisibleCells().map((cell) => (
                      <TableCell key={cell.id} className="text-sm">
                        {flexRender(
                          cell.column.columnDef.cell,
                          cell.getContext(),
                        )}
                      </TableCell>
                    ))}
                  </motion.tr>
                ))
              )}
            </TableBody>
          </Table>
        </div>
      </div>

      <Pagination
        page={page.page}
        pageSize={page.size}
        totalItems={query.data?.page?.totalElements ?? 0}
        onPageChange={(page) => updatePagination({ page })}
        onPageSizeChange={(size) => updatePagination({ size, page: 0 })}
      />
    </div>
  );
}
